package com.jy.helpring.web.dto.cart;

import com.jy.helpring.domain.cart.Cart;
import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CartDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto{
        private Long id;
        private Long member_id;
        private Long lecture_id;

        public Cart toEntity(Member member, Lecture lecture){
            Cart cart = Cart.builder()
                    .id(id)
                    .member(member)
                    .lecture(lecture)
                    .build();

            return cart;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{

        private Long id;
        private Long member_id;
        private Long lecture_id;

        /* 장바구니에서 보여줄 것 */
        private String lecture_title;
        private int lecture_price;
        private String lecture_fileName;

        public ResponseDto(Cart cart){
            this.id = cart.getId();
            this.member_id = cart.getMember().getId();
            this.lecture_id = cart.getLecture().getId();

            this.lecture_title = cart.getLecture().getTitle();
            this.lecture_price = cart.getLecture().getPrice();
            this.lecture_fileName = cart.getLecture().getFileName();
        }
    }
}
