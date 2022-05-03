package com.jy.helpring.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByMember_IdAndLecture_Id(Long member_id, Long lecture_id);
}
