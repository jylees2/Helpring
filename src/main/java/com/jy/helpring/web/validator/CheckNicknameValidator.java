package com.jy.helpring.web.validator;

import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.web.dto.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class CheckNicknameValidator extends AbstractValidator<MemberDto.RequestDto>{

    private final MemberRepository memberRepository;

    @Override
    protected void doValidate(MemberDto.RequestDto dto, Errors errors) {
        if (memberRepository.existsByNickname(dto.toEntity().getNickname())) {
            /* 중복인 경우 */
            errors.rejectValue("nickname", "닉네임 중복 오류", "이미 사용 중인 닉네임입니다.");
        }
    }
}
