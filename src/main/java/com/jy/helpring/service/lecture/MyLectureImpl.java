package com.jy.helpring.service.lecture;

import com.jy.helpring.domain.cart.Cart;
import com.jy.helpring.domain.cart.CartRepository;
import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.LectureRepository;
import com.jy.helpring.domain.lecture.MyLecture;
import com.jy.helpring.domain.lecture.MyLectureRepository;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MyLectureImpl implements MyLectureService{

    MemberRepository memberRepository;
    LectureRepository lectureRepository;
    MyLectureRepository myLectureRepository;
    CartRepository cartRepository;

    /** 구매한 강의에 대한 권한 부여 **/
    @Override
    public void getLecture(Long member_id) {

        /* MyLecture 엔티티에 회원, 강의 정보 추가 */
        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        /** member_id 에 해당하는 Cart 엔티티 리스트를 돌면서 각각 MyLecture 엔티티에 저장 **/
        // 장바구니에 있는 강의를 구매했으므로 전부 MyLecture 엔티티에 저장한다는 의미
        Lecture lecture = new Lecture();
        for(Cart cart : cartRepository.findAllByMember_Id(member_id)){
            lecture = lectureRepository.findById(cart.getLecture().getId()).orElseThrow(() ->
                                        new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

            // member, lecture 각각 넣은 MyLecture 엔티티 저장장
           myLectureRepository.save(MyLecture.builder().member(member).lecture(lecture).build());
        }
    }
}
