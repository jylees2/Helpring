package com.jy.helpring.service.review;

import com.jy.helpring.web.dto.review.ReviewDto;

import java.util.List;

public interface ReviewService {

    /** lecture_id 에 대한 리뷰 목록 조회 **/
    List<ReviewDto.ResponseDto> findAllByLecture(Long lecture_id);

    /** lecture_id, member_id에 대한 리뷰 목록이 존재하는지 여부 - 유저가 특정 강의에 리뷰를 작성했는지 확인 **/
    boolean reviewCheck(Long member_id, Long lecture_id);

    /** 리뷰 저장 **/
    Long save(Long member_id, Long lecture_id, ReviewDto.RequestDto requestDto);

    /** 리뷰 삭제 **/
    void delete(Long review_id);
}
