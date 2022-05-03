package com.jy.helpring.service.cart;

import com.jy.helpring.domain.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;

    /** member_id의 장바구니 전부 삭제 **/
    @Override
    public void deleteAll(Long member_id) {
        cartRepository.deleteAllByMember_Id(member_id);
    }
}
