package com.jy.helpring.service.member;

import com.jy.helpring.web.dto.member.MemberDto;

public interface MemberService {

    void userJoin(MemberDto.RequestDto memberDto);

    void userInfoUpdate(MemberDto.RequestDto memberDto);

    /** member_id로 memberDto 반환 **/
    MemberDto.ResponseDto getById(Long member_id);

    /** 비밀번호 일치 확인 **/
    boolean checkPassword(Long member_id, String checkPassword);

    /** 이메일 존재 확인 **/
    String checkEmail(String memberEmail);

    /** 임시 비밀번호 생성 **/
    String getTmpPassword();

    /** 임시 비밀번호로 업데이트 **/
    void updatePassword(String tmpPassword, String memberEmail);

}
