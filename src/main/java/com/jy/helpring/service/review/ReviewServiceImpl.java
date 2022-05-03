package com.jy.helpring.service.review;

import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.LectureRepository;
import com.jy.helpring.domain.review.Review;
import com.jy.helpring.domain.review.ReviewRepository;
import com.jy.helpring.web.dto.review.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final LectureRepository lectureRepository;
    private final ReviewRepository reviewRepository;

    /** lecture_id 에 대한 리뷰 리스트 조회 **/
    @Override
    public List<ReviewDto.ResponseDto> findAllByLecture(Long lecture_id) {

        Lecture lecture = lectureRepository.findById(lecture_id).orElseThrow(() ->
                                                new IllegalArgumentException("해당 강의가 없습니다."));

        List<Review> reviewList = lecture.getReview();

        return reviewList.stream().map(ReviewDto.ResponseDto::new).collect(Collectors.toList());
    }

    /** lecture_id, member_id에 대한 리뷰 리스트가 존재하는지 여부 **/
    @Override
    public boolean reviewCheck(Long member_id, Long lecture_id) {
        if(reviewRepository.findByMember_IdAndLecture_Id(member_id, lecture_id) == null){
            return false;
        } else {
            return true;
        }
    }
}
