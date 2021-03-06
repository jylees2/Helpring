package com.jy.helpring.web.controller.lecture;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.service.lecture.MemberWishLectureService;
import com.jy.helpring.web.dto.lecture.MemberWishLectureDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/wish")
@RequiredArgsConstructor
@Slf4j
public class MemberWishLectureController {

    private final MemberWishLectureService memberWishLectureService;

    /** 내 찜 목록 반환 **/
    @GetMapping("/myWish")
    public String findAll(@AuthenticationPrincipal UserAdapter user,
                          Model model){

        Long member_id = user.getMemberDto().getId();

        /* 내가 찜한 목록이 존재하는지 확인 */
        boolean checkWish = memberWishLectureService.checkHaveWish(member_id);

        if(checkWish){
            List<MemberWishLectureDto.ResponseDto> wishList = memberWishLectureService.findByMemberId(member_id);
            model.addAttribute("wishList", wishList);
        }

        model.addAttribute("checkWish", checkWish);

        return "wish/myWish";
    }


}
