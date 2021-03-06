package com.jy.helpring.service.cart;

import com.jy.helpring.web.dto.cart.CartDto;

import java.util.List;

public interface CartService {

    /** member_id에 해당하는 장바구니 존재 여부 확인 - 유저의 장바구니 목록이 존재하는지 확인 **/
    boolean checkHaveCart(Long member_id);

    /** 장바구니에서 모든 강의 삭제 **/
    void deleteAll(Long member_id);

    /** 장바구니에서 특정 강의 삭제 **/
    void deleteById(Long cart_id);

    /** 장바구니 리스트 반환 **/
    List<CartDto.ResponseDto> getListMemberId(Long member_id);

    /** 장바구니에 강의가 존재하는지 확인 **/
    boolean checkCart(Long member_id, Long lecture_id);

    /** 장바구니 저장 **/
    void save(Long member_id, Long lecture_id);
}
