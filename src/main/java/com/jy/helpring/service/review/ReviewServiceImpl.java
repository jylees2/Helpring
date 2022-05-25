package com.jy.helpring.service.review;

import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.LectureRepository;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.domain.review.Review;
import com.jy.helpring.domain.review.ReviewRepository;
import com.jy.helpring.web.dto.review.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService{

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final ReviewRepository reviewRepository;

    /** lecture_id 에 대한 리뷰 목록 조회 **/
    @Override
    public List<ReviewDto.ResponseDto> findAllByLecture(Long lecture_id) {

        Lecture lecture = lectureRepository.findById(lecture_id).orElseThrow(() ->
                                                new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        List<Review> reviewList = lecture.getReview();

        return reviewList.stream().map(ReviewDto.ResponseDto::new).collect(Collectors.toList());
    }

    /** lecture_id, member_id에 대한 리뷰 목록이 존재하는지 여부 - 유저가 특정 강의에 리뷰를 작성했는지 확인 **/
    @Override
    public boolean reviewCheck(Long member_id, Long lecture_id) {

        return reviewRepository.existsByMember_IdAndLecture_Id(member_id, lecture_id);

    }

    /** 리뷰 저장 **/
    @Override
    public Long save(Long member_id, Long lecture_id, ReviewDto.RequestDto requestDto) {

        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Lecture lecture = lectureRepository.findById(lecture_id).orElseThrow(() ->
                new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        Review review = requestDto.toEntity(member, lecture);
        reviewRepository.save(review);

        return review.getId();
    }

    /** 리뷰 삭제 **/
    @Override
    public void delete(Long review_id) {

        Review review = reviewRepository.findById(review_id).orElseThrow(() ->
                new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        reviewRepository.delete(review);
        log.info("리뷰 삭제 완료");
    }
}
