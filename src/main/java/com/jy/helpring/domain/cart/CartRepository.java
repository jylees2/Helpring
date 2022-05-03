package com.jy.helpring.domain.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    /** member_id 에 해당하는 엔티티 모두 반환 **/
    List<Cart> findAllByMember_Id(Long member_id);

    /** member_id 에 해당하는 엔티티 모두 삭제 **/
    void deleteAllByMember_Id(Long member_id);
}
