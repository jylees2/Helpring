package com.jy.helpring.web.controller.cart;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.service.cart.CartService;
import com.jy.helpring.web.dto.cart.CartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    /** 내 장바구니 조회 **/
    @GetMapping("/")
    public String findAll(@AuthenticationPrincipal UserAdapter user,
                          Model model){

        /* 현재 로그인한 유저, 즉 구매자 정보 반환 */
        Long member_id = user.getMemberDto().getId();
        model.addAttribute("user", user.getMemberDto());

        /* 장바구니 존재 여부 확인 */
        boolean checkCart = cartService.checkHaveCart(member_id);

        if(checkCart){

            /* 장바구니가 존재한다면 */
            int totalPrice = 0;

            /* 장바구니 목록 반환 */
            List<CartDto.ResponseDto> cartList = cartService.getListMemberId(member_id);

            // 가격 총계 계산
            for(CartDto.ResponseDto cart : cartList){
                totalPrice += cart.getLecture_price();
            }

            model.addAttribute("cartList", cartList);
            model.addAttribute("totalPrice", totalPrice);
        }

        /* 장바구니 존재 여부 반환 */
        model.addAttribute("checkCart", checkCart);

        return "cart/cart";
    }
}
