package com.jy.helpring.domain.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    /** member_id에 해당하는 cart 존재 여부 반환 - 유저의 장바구니가 존재하는지 확인 **/
    boolean existsByMember_Id(Long member_id);

    /** member_id, lecture_id에 해당하는 cart 엔티티 존재 여부 반환 - 유저가 특정 강의를 장바구니에 추가했는지 확인 **/
    boolean existsByMember_IdAndLecture_Id(Long member_id, Long lecture_id);

    /** member_id 에 해당하는 엔티티 리스트 반환 - 유저의 장바구니 목록 반환 **/
    List<Cart> findByMember_Id(Long member_id);

    /** member_id 에 해당하는 엔티티 모두 삭제 - 유저의 장바구니 목록 모두 삭제 **/
    void deleteAllByMember_Id(Long member_id);




}
