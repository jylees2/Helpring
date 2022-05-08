package com.jy.helpring.web.controller.member;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.service.member.MemberService;
import com.jy.helpring.web.dto.member.MemberDto;
import com.jy.helpring.web.validator.CheckEmailValidator;
import com.jy.helpring.web.validator.CheckNicknameValidator;
import com.jy.helpring.web.validator.CheckUsernameValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /** 중복 체크 유효성 검사 **/
    private final CheckUsernameValidator checkUsernameValidator;
    private final CheckNicknameValidator checkNicknameValidator;
    private final CheckEmailValidator checkEmailValidator;

    /** 커스텀 유효성 검증 **/
    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkUsernameValidator);
        binder.addValidators(checkNicknameValidator);
        binder.addValidators(checkEmailValidator);
    }

    /** 회원가입 폼으로 이동 **/
    @GetMapping("/auth/join")
    public String join() {
        return "member/member-join";
    }


    /** 회원가입 과정 **/
    @PostMapping("/auth/joinProc")
    public String joinProc(@Valid MemberDto.RequestDto memberDto, BindingResult bindingResult, Model model) {

        /** 검증 **/

        /** 회원가입 실패 시 **/
        if (bindingResult.hasErrors()) {
            /* 회원 가입 실패 시 입력 데이터 값 유지 */
            model.addAttribute("member", memberDto);

            /* 유효성 검사를 통과하지 못 한 필드와 메시지 핸들링 */
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put("valid_" + error.getField(), error.getDefaultMessage());
                log.info("회원 가입 실패 ! error message : " + error.getDefaultMessage());
            }

            /* Model에 담아 view resolve */
            for (String key : errorMap.keySet()) {
                model.addAttribute(key, errorMap.get(key));
            }

            /* 회원가입 페이지로 리턴 */
            return "member/member-join";
        }

        /** 회원 가입 성공 시 로그인 페이지로 리다이렉트 **/
        memberService.userJoin(memberDto);
        log.info("회원가입 성공");
        return "redirect:/auth/login";
    }

    /** 로그인 **/
    @GetMapping("/auth/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model) {

        /** 에러와 예외를 모델에 담아 view resolve **/
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "member/member-login";
    }

    /** 로그아웃 - 기본적으로 스프링 시큐리티에서는 로그아웃이 POST 요청 방식이지만 GET 방식으로 우회 **/
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        /** 인증 확인, 인증 객체를 꺼내옴 **/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        /** SecurityContextLogoutHandler 통해 logout **/
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        log.info("로그아웃 성공");
        return "redirect:/"; // 메인 페이지로 리다이렉트
    }

    /** 회원 수정 페이지 **/
    @GetMapping("/member/modify")
    public String UserInfoModify(@AuthenticationPrincipal UserAdapter user,
                                 Model model) {
        if (user != null) {
            model.addAttribute("user", user);
        }

        return "member/member-modify";
    }

    /** 비밀번호 찾기 - 임시 비밀번호 발급 **/

}