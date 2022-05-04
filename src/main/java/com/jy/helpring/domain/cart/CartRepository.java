package com.jy.helpring.domain.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    /** member_id 에 해당하는 엔티티 리스트 반환 **/
    List<Cart> findByMember_Id(Long member_id);

    /** member_id 에 해당하는 엔티티 모두 삭제 **/
    void deleteAllByMember_Id(Long member_id);

    /** member_id, lecture_id 에 해당하는 cart 엔티티 반환 **/
    Optional<Cart> findByMember_IdAndLecture_Id(Long member_id, Long lecture_id);


}
