package com.jy.helpring.service.review;

import com.jy.helpring.web.dto.review.ReviewDto;

import java.util.List;

public interface ReviewService {

    /** lecture_id 에 대한 리뷰 리스트 조회 **/
    List<ReviewDto.ResponseDto> findAllByLecture(Long lecture_id);

    /** lecture_id, member_id에 대한 리뷰 리스트가 존재하는지 여부 **/
    boolean reviewCheck(Long member_id, Long lecture_id);
}
