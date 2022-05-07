package com.jy.helpring.web.validator;

import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.web.dto.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class CheckEmailValidator extends AbstractValidator<MemberDto.RequestDto> {

    private final MemberRepository memberRepository;

    @Override
    protected void doValidate(MemberDto.RequestDto dto, Errors errors) {
        if (memberRepository.existsByEmail(dto.toEntity().getEmail())) {
            /* 중복인 경우 */
            errors.rejectValue("email", "이메일 중복 오류", "이미 사용 중인 이메일입니다.");
        }
    }
}
