package com.jy.helpring.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyLectureRepository extends JpaRepository<MyLecture, Long> {

    /** member_id, lecture_id로 MyLecture 엔티티에 있는 정보 반환 **/
    List<MyLecture> findByMember_IdAndLecture_Id(Long member_id, Long lecture_id);
}
