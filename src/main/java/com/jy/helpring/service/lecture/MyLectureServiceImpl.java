package com.jy.helpring.service.lecture;

import com.jy.helpring.domain.cart.Cart;
import com.jy.helpring.domain.cart.CartRepository;
import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.LectureRepository;
import com.jy.helpring.domain.lecture.MyLecture;
import com.jy.helpring.domain.lecture.MyLectureRepository;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.web.dto.mylecture.MyLectureDto;
import com.jy.helpring.web.vo.PageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MyLectureServiceImpl implements MyLectureService{

    private static final int PAGE_MYLECTURE_COUNT = 10; // 한 화면에 보일 컨텐츠 수

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final MyLectureRepository myLectureRepository;
    private final CartRepository cartRepository;

    /** 구매한 강의에 수강 권한 부여 **/
    @Override
    public void getLecture(Long member_id) {

        /* MyLecture 엔티티에 회원, 강의 정보 추가 */
        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        /** member_id 에 해당하는 Cart 엔티티 리스트를 돌면서 각각 MyLecture 엔티티에 저장 **/
        // 장바구니에 있는 강의를 구매했으므로 전부 MyLecture 엔티티에 저장한다는 의미
        Lecture lecture = new Lecture();
        for(Cart cart : cartRepository.findByMember_Id(member_id)){
            lecture = lectureRepository.findById(cart.getLecture().getId()).orElseThrow(() ->
                                        new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

            // member, lecture 각각 넣은 MyLecture 엔티티 저장
           myLectureRepository.save(MyLecture.builder().member(member).lecture(lecture).build());
        }
    }

    /** 유저가 구매한 강의 목록이 존재하는지 확인 **/
    @Override
    public boolean checkLecture(Long member_id) {
        return myLectureRepository.existsByMember_Id(member_id);
    }

    /** 유저가 구매한 강의 목록 페이징 처리 반환 **/
    @Override
    public Page<MyLectureDto.ResponsePageDto> getAllPageList(Long member_id, Pageable pageable, int pageNo) {

        /* pageable 객체 반환 */
        PageRequest.of(pageNo, PAGE_MYLECTURE_COUNT, Sort.by(Sort.Direction.DESC, "id"));

        /* member_id에 해당하는 myLecture 페이지 객체 반환 */
        Page<MyLecture> page = myLectureRepository.findByMember_Id(member_id, pageable);

        /* Dto로 변환 */
        Page<MyLectureDto.ResponsePageDto> myLecturePageList = page.map(
                myLecture -> new MyLectureDto.ResponsePageDto(
                        myLecture.getId(),
                        myLecture.getLecture().getId(),
                        myLecture.getLecture().getTitle(),
                        myLecture.getLecture().getIntro(),
                        myLecture.getLecture().getFileName(),
                        myLecture.getLecture().getCategory().getName(),
                        myLecture.getLecture().getCategory().getViewName()
                )
        );
        return myLecturePageList;
    }

    /** 페이징 정보 반환 **/
    @Override
    public PageVo getPageInfo(Page<MyLectureDto.ResponsePageDto> myLecturePageList, int pageNo) {
        int totalPage = myLecturePageList.getTotalPages();


        // 현재 페이지를 통해 현재 페이지 그룹의 시작 페이지를 구함
        int startNumber = (int)((Math.floor(pageNo/PAGE_MYLECTURE_COUNT)*PAGE_MYLECTURE_COUNT)+1 <= totalPage ? (Math.floor(pageNo/PAGE_MYLECTURE_COUNT)*PAGE_MYLECTURE_COUNT)+1 : totalPage);

        // 전체 페이지 수와 현재 페이지 그룹의 시작 페이지를 통해 현재 페이지 그룹의 마지막 페이지를 구함
        int endNumber = (startNumber + PAGE_MYLECTURE_COUNT-1 < totalPage ? startNumber + PAGE_MYLECTURE_COUNT-1 : totalPage);
        boolean hasPrev = myLecturePageList.hasPrevious();
        boolean hasNext = myLecturePageList.hasNext();

		/* 화면에는 원래 페이지 인덱스+1 로 출력됨을 주의 */		
        int prevIndex = myLecturePageList.previousOrFirstPageable().getPageNumber()+1;
        int nextIndex = myLecturePageList.nextOrLastPageable().getPageNumber()+1;

        return new PageVo(totalPage, startNumber, endNumber, hasPrev, hasNext, prevIndex, nextIndex);    
        }
}
