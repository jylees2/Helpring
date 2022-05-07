package com.jy.helpring.web.controller.member;

import com.jy.helpring.service.member.MemberService;
import com.jy.helpring.web.dto.member.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
@Slf4j
public class MemberRestController {

    /**
     * 회원 정보 수정 후 재로그인하지 않아도 바로 사용자 화면에 수정된 정보가 나올 수 있게 함
     * 정보 수정을 하면 DB에는 데이터가 변강 되지만 현재 수정 전 세션을 갖고 있다면
     * 사용자 화면에서는 수정 전 정보가 나오는 문제 발생할 수 있음
     */
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    @PutMapping("/member")
    public ResponseEntity modify(@RequestBody MemberDto.RequestDto dto) {

        /** 회원 정보 변경 **/
        memberService.userInfoModify(dto);

        log.info("MemberRestController 진입");

        /** ========== 변경된 세션 등록 ========== **/
        /* 1. 새로운 UsernamePasswordAuthenticationToken 생성하여 AuthenticationManager 을 이용해 등록 */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        /* 2. SecurityContextHolder 안에 있는 Context를 호출해 변경된 Authentication으로 설정 */
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity(HttpStatus.OK);
    }
}
