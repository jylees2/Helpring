package com.jy.helpring.service.member;

import com.jy.helpring.domain.member.Member;
import com.jy.helpring.web.dto.member.MemberDto;

public interface MemberService {

    /** 회원가입 **/
    void userJoin(MemberDto.RequestDto memberDto);

    /** member_id로 memberDto 반환 **/
    MemberDto.ResponseDto getById(Long member_id);

    /** =============== 회원 수정 =============== **/

    /** 닉네임 중복 체크 **/
    boolean checkNickname(Long member_id, String nickname);

    /** 비밀번호 일치 확인 **/
    boolean checkPassword(Long member_id, String checkPassword);

    /** 회원 수정 **/
    void userInfoUpdate(MemberDto.RequestDto memberDto);

    /** =============== 비밀번호 찾기 : 임시 비밀번호 전송 =============== **/

    /** 이메일 존재 확인 **/
    boolean checkEmail(String memberEmail);

    /** 임시 비밀번호 생성 **/
    String getTmpPassword();

    /** 임시 비밀번호로 업데이트 **/
    void updatePassword(String tmpPassword, String memberEmail);

}
