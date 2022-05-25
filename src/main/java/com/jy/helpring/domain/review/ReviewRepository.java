package com.jy.helpring.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /** 유저가 특정 강의에 대한 리뷰를 작성했는지 확인 **/
    boolean existsByMember_IdAndLecture_Id(Long member_id, Long lecture_id);

}
