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

@Slf4j
@Controller
@RequestMapping("/wish")
@RequiredArgsConstructor
public class MemberWishLectureController {

    private final MemberWishLectureService memberWishLectureService;

    @GetMapping("/myWish")
    public String findAll(@AuthenticationPrincipal UserAdapter user,
                          Model model){

        Long member_id = user.getMemberDto().getId();
        List<MemberWishLectureDto.ResponseDto> wishList = memberWishLectureService.findByMemberId(member_id);

        model.addAttribute("wishList", wishList);

        return "wish/myWish";
    }


}
