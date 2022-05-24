package com.jy.helpring.domain.lecture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyLectureRepository extends JpaRepository<MyLecture, Long> {

    /** member_id, lecture_id로 MyLecture 엔티티에 있는 정보 반환 **/
    List<MyLecture> findByMember_IdAndLecture_Id(Long member_id, Long lecture_id);

    /** member_id 로 MyLecture 엔티티에 강의 리스트가 존재하는지 확인 **/
    boolean existsByMember_Id(Long member_id);

    /** member_id로 MyLecture 엔티티에 있는 강의 리스트 반환 **/
    Page<MyLecture> findByMember_Id(Long member_Id, Pageable pageable);
}
