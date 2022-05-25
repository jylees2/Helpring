package com.jy.helpring.web.controller.member;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.service.mail.MailService;
import com.jy.helpring.service.member.MemberService;
import com.jy.helpring.web.dto.member.MemberDto;
import com.jy.helpring.web.vo.MailVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
@Slf4j
public class MemberRestController {

    /**
     * 회원 정보 수정 후 재로그인하지 않아도 바로 사용자 화면에 수정된 정보가 나올 수 있게 함
     * 정보 수정을 하면 DB에는 데이터가 변경 되지만 현재 수정 전 세션을 갖고 있다면
     * 사용자 화면에서는 수정 전 정보가 나오는 문제 발생할 수 있음
     */
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    /** 회원 정보 수정 **/
    @PutMapping("/member")
    public boolean update(@RequestBody MemberDto.RequestDto dto) {

        log.info("MemberRestController 진입");

        if(memberService.checkNickname(dto.getId(), dto.getNickname())){
            log.info("중복 닉네임");
            return false;
        } else{
            log.info("사용 가능한 닉네임");

            // 회원 정보 수정
            memberService.userInfoUpdate(dto);

            /** ========== 변경된 세션 등록 ========== **/
            /* 1. 새로운 UsernamePasswordAuthenticationToken 생성하여 AuthenticationManager 을 이용해 등록 */
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );

            /* 2. SecurityContextHolder 안에 있는 Context를 호출해 변경된 Authentication으로 설정 */
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        }
    }

    /** 회원 수정 전 비밀번호 확인 **/
    @GetMapping("/checkPwd")
    public boolean checkPassword(@AuthenticationPrincipal UserAdapter user,
                                @RequestParam String checkPassword,
                                Model model){

        log.info("checkPwd 진입");
        Long member_id = user.getMemberDto().getId();

        return memberService.checkPassword(member_id, checkPassword);
    }

    /** 이메일이 DB에 존재하는지 확인 **/
    @GetMapping("/checkEmail")
    public boolean checkEmail(@RequestParam("memberEmail") String memberEmail){

        log.info("checkEmail 진입");
        return memberService.checkEmail(memberEmail);
    }

}
