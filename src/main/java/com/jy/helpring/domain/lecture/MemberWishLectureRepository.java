package com.jy.helpring.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberWishLectureRepository extends JpaRepository<MemberWishLecture, Long> {

    /** member_id에 해당하는 찜 리스트가 존재하는지 확인 - 유저의 찜 목록 여부 **/
    boolean existsByMember_Id(Long member_id);

    /** member_id, lecture_id에 해당하는 wish 엔티티 존재 여부 반환 - 유저가 특정 강의를 찜 목록에 추가했는지 확인 **/
    boolean existsByMember_IdAndLecture_Id(Long member_id, Long lecture_id);

    /** member_id, lecture_id에 해당하는 찜 엔티티 삭제 - 유저가 특정 강의 삭제 **/
    void deleteByMember_IdAndLecture_Id(Long member_id, Long lecture_id);


}
