package com.jy.helpring.web.controller.cart;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartRestController {

    private final CartService cartService;

    /** 장바구니에 강의 저장 **/
    @PostMapping("/{lecture_id}")
    public boolean save(@PathVariable Long lecture_id,
                        @AuthenticationPrincipal UserAdapter user){

        Long member_id = user.getMemberDto().getId();
        if(cartService.checkCart(member_id, lecture_id)){
            /* cart가 존재한다면 */
            return true;
        } else {
            /* cart가 존재하지 않는다면 장바구니에 저장 */
            cartService.save(member_id, lecture_id);
            return false;
        }
    }

    /** 장바구니에서 특정 강의 삭제 **/
    @DeleteMapping("/{cart_id}")
    public ResponseEntity deleteById(@PathVariable Long cart_id){

        cartService.deleteById(cart_id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
