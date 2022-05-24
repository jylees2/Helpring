package com.jy.helpring.service.lecture;

import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.LectureRepository;
import com.jy.helpring.domain.lecture.MemberWishLecture;
import com.jy.helpring.domain.lecture.MemberWishLectureRepository;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.web.dto.lecture.MemberWishLectureDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberWishLectureServiceImpl implements MemberWishLectureService{

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final MemberWishLectureRepository memberWishLectureRepository;

    /** 찜 리스트 반환 **/
    @Override
    public List<MemberWishLectureDto.ResponseDto> findByMemberId(Long member_id) {
        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        List<MemberWishLecture> wishList = member.getWish();

        return wishList.stream().map(MemberWishLectureDto.ResponseDto::new).collect(Collectors.toList());
    }

    /** member_id에 해당하는 찜 리스트 존재 여부 확인 **/
    @Override
    public boolean checkHaveWish(Long member_id) {
        return memberWishLectureRepository.existsByMember_Id(member_id);
    }

    /** 해당 강의를 찜했는지 확인 **/
    @Override
    public boolean checkWish(Long member_id, Long lecture_id) {

        Optional<MemberWishLecture> wish = memberWishLectureRepository.findByMember_IdAndLecture_Id(member_id, lecture_id);
        if(!wish.isEmpty()){
            /* 찜한 강의라면 */
            return true;
        } else{
            /* 찜한 강의가 아니라면 */
            return false;
        }
    }

    /** 찜 리스트에 추가 **/
    @Override
    public void save(Long member_id, Long lecture_id) {

        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Lecture lecture = lectureRepository.findById(lecture_id).orElseThrow(() ->
                new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        memberWishLectureRepository.save(MemberWishLecture.builder().member(member).lecture(lecture).build());
    }

    /** 찜 리스트에서 member_id, lecture_id에 해당하는 찜 삭제 **/
    @Override
    public void deleteById(Long member_id, Long lecture_id) {
        memberWishLectureRepository.deleteByMember_IdAndLecture_Id(member_id, lecture_id);
    }

    /** 찜 리스트에서 wish_id에 해당하는 찜 삭제 **/
    @Override
    public void deleteWish(Long wish_id) {
        MemberWishLecture wish = memberWishLectureRepository.findById(wish_id).orElseThrow(() ->
                new IllegalArgumentException("해당 찜이 존재하지 않습니다."));
        memberWishLectureRepository.delete(wish);
    }
}
