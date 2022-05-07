package com.jy.helpring.service.member;

import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.service.post.PostService;
import com.jy.helpring.web.dto.member.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final PostService postService;

    /** 회원가입 **/
    @Override
    public void userJoin(MemberDto.RequestDto memberDto) {
        /* 비밀번호 암호화 */
        memberDto.encryptPassword(encoder.encode(memberDto.getPassword()));

        Member member = memberDto.toEntity();
        memberRepository.save(member);

        log.info("회원 저장 성공");
    }

    /** 회원 수정 **/
    @Override
    public void userInfoModify(MemberDto.RequestDto memberDto) {

        /* 회원 찾기 */
        Member member = memberRepository.findById(memberDto.toEntity().getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        /* 수정한 비밀번호 암호화 */
        String encryptPassword = encoder.encode(memberDto.getPassword());
        member.modify(memberDto.getNickname(), encryptPassword); // 회원 수정

        log.info("회원 수정 성공");
    }
}
