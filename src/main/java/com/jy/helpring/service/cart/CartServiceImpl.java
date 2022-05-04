package com.jy.helpring.service.cart;

import com.jy.helpring.domain.cart.Cart;
import com.jy.helpring.domain.cart.CartRepository;
import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.LectureRepository;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.web.dto.cart.CartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;

    /** member_id의 장바구니 전부 삭제 **/
    @Override
    public void deleteAll(Long member_id) {
        cartRepository.deleteAllByMember_Id(member_id);
    }

    /** 장바구니에서 특정 강의 삭제 **/
    @Override
    public void deleteById(Long cart_id) {
        Cart cart = cartRepository.findById(cart_id).orElseThrow(() ->
                new IllegalArgumentException("해당 장바구니 정보가 존재하지 않습니다."));

        cartRepository.delete(cart);
    }

    /** member_id을 통해 cartList 받아옴 **/
    @Override
    public List<CartDto.ResponseDto> getListMemberId(Long member_id) {

        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        List<Cart> cartList = member.getCart();

        return cartList.stream().map(CartDto.ResponseDto::new).collect(Collectors.toList());
    }

    /** member_id, lecture_id에 해당하는 cart가 존재하는지 확인 **/
    @Override
    public boolean checkCart(Long member_id, Long lecture_id) {

        Optional<Cart> cart = cartRepository.findByMember_IdAndLecture_Id(member_id, lecture_id);
        if(cart.isEmpty()){
            return false;
        }
        return true;
    }

    /** 장바구니 저장 **/
    @Override
    public void save(Long member_id, Long lecture_id) {
        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        Lecture lecture = lectureRepository.findById(lecture_id).orElseThrow(() ->
                new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        /* DB에 cart 엔티티 저장  */
        cartRepository.save(Cart.builder().member(member).lecture(lecture).build());
    }
}
