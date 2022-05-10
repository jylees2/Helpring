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

        Long member_id = user.getMemberDto().getId();

        /** 카트 리스트  반환 **/
        List<CartDto.ResponseDto> cartList = cartService.getListMemberId(member_id);

        boolean checkCart = true;
        if(!cartList.isEmpty()){
            /* 장바구니가 존재한다면 */
            int totalPrice = 0;

            for(CartDto.ResponseDto cart : cartList){
                totalPrice += cart.getLecture_price();
            }

            model.addAttribute("cartList", cartList);
            model.addAttribute("totalPrice", totalPrice);
        } else {
            /* 장바구니가 비어있다면 */
            checkCart = false;
        }

        model.addAttribute("checkCart", checkCart);

        return "cart/cart";
    }
}
