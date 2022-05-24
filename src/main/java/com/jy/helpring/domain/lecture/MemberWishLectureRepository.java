package com.jy.helpring.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberWishLectureRepository extends JpaRepository<MemberWishLecture, Long> {

    /** member_id 에 해당하는 찜 리스트 반환 **/
    Optional<MemberWishLecture> findByMember_IdAndLecture_Id(Long member_id, Long lecture_id);
    void deleteByMember_IdAndLecture_Id(Long member_id, Long lecture_id);

    /** member_id에 해당하는 찜 리스트가 존재하는지 확인 **/
    boolean existsByMember_Id(Long member_id);
}
