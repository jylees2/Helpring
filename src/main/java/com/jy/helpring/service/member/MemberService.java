package com.jy.helpring.service.member;

import com.jy.helpring.web.dto.member.MemberDto;

public interface MemberService {

    void userJoin(MemberDto.RequestDto memberDto);

    void userInfoModify(MemberDto.RequestDto memberDto);
}
