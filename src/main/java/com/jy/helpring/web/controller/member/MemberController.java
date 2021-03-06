package com.jy.helpring.web.controller.member;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.service.lecture.MyLectureService;
import com.jy.helpring.service.mail.MailService;
import com.jy.helpring.service.member.MemberService;
import com.jy.helpring.web.dto.member.MemberDto;
import com.jy.helpring.web.dto.mylecture.MyLectureDto;
import com.jy.helpring.web.validator.CheckEmailValidator;
import com.jy.helpring.web.validator.CheckNicknameValidator;
import com.jy.helpring.web.validator.CheckUsernameValidator;
import com.jy.helpring.web.vo.MailVo;
import com.jy.helpring.web.vo.PageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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
    private final MailService mailService;
    private final MyLectureService myLectureService;

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
    public String join(Model model) {

        model.addAttribute("memberDto", new MemberDto.RequestDto());
        return "member/member-join";
    }


    /** 회원가입 과정 **/
    @PostMapping("/auth/joinProc")
    public String joinProc(@ModelAttribute @Valid MemberDto.RequestDto memberDto,
                           BindingResult bindingResult,
                           Model model) {

        log.info("회원가입 과정 진입");

        /** 검증 **/

        /** 회원가입 실패 시 **/
        if (bindingResult.hasErrors()) {

            /* 회원 가입 실패 시 입력 데이터 값 유지 */
            log.info("회원 가입 과정에 에러 존재");
            model.addAttribute("memberDto", memberDto);

            /* 유효성 검사를 통과하지 못 한 필드와 메시지 핸들링 */
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put("valid_" + error.getField(), error.getDefaultMessage());
                log.info("회원 가입 실패 ! error message : " + error.getDefaultMessage());
            }

            /* Model에 error를 담아 view resolve */
            for (String key : errorMap.keySet()) {
                log.info(key);
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

        /** 에러와 예외가 존재하는 경우우 모델에 담아 view resolv **/
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
    @GetMapping("/settings/update")
    public String UserInfoModify(@AuthenticationPrincipal UserAdapter user,
                                 Model model) {

        /* 회원 정보 객체 반환 */
        Long member_id = user.getMemberDto().getId();
        MemberDto.ResponseDto responseDto = memberService.getById(member_id);
        model.addAttribute("member", responseDto);

        return "member/member-update";
    }

    /** 회원 수정하기 전 비밀번호 확인 **/
    @GetMapping("/checkPwd")
    public String checkPwdView(){
        return "member/check-pwd";
    }

    /** 마이페이지 **/
    @GetMapping("/myPage")
    public String findByMemberId(@AuthenticationPrincipal UserAdapter user,
                                 Model model) {

        Long member_id = user.getMemberDto().getId();
        MemberDto.ResponseDto responseDto = memberService.getById(member_id);
        model.addAttribute("member", responseDto);
        return "member/myPage";
    }

    /** 비밀번호 찾기 - 임시 비밀번호 발급 **/

    @PostMapping("/sendPwd")
    public String sendPwdEmail(@RequestParam("memberEmail") String memberEmail) {

        log.info("sendPwdEmail 진입");
        log.info("이메일 : "+ memberEmail);

        /** 임시 비밀번호 생성 **/
        String tmpPassword = memberService.getTmpPassword();

        /** 임시 비밀번호 저장 **/
        memberService.updatePassword(tmpPassword, memberEmail);

        /** 메일 생성 & 전송 **/
        MailVo mail = mailService.createMail(tmpPassword, memberEmail);
        mailService.sendMail(mail);

        log.info("임시 비밀번호 전송 완료");

        return "member/member-login";
    }

    /** 내가 수강 중인 강의 조회 **/
    /* /myLecture */
    @GetMapping("/myLecture")
    public String myLecture(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                            Pageable pageable,
                            @AuthenticationPrincipal UserAdapter user,
                            Model model){

        Long member_id = user.getMemberDto().getId();

        boolean myLectureCheck = false;
        if(myLectureService.checkLecture(member_id)){
            myLectureCheck = true;

            // 구매한 강의가 존재하면 강의 리스트 반환

            /** ========== 페이징 처리 ========== **/
            pageNo = (pageNo == 0) ? 0 : (pageNo - 1);

            Page<MyLectureDto.ResponsePageDto> myLecturePageList =
                    myLectureService.getAllPageList(member_id, pageable, pageNo); // 페이지 객체 생성

            // 페이징 정보 반환
            PageVo pageVo = myLectureService.getPageInfo(myLecturePageList, pageNo);

            model.addAttribute("myLectureList", myLecturePageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageVo", pageVo);
        } else {
            // 구매한 강의가 존재하지 않으면
            myLectureCheck = false;
        }

        model.addAttribute("myLectureCheck", myLectureCheck);
        model.addAttribute("user", user);

        return "lecture/myLecture";
    }
}
