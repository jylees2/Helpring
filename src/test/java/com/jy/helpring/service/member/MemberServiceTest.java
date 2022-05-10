package com.jy.helpring.service.member;

import com.jy.helpring.domain.Role;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.web.dto.member.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Slf4j
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    String username = "minha";
    String rawPassword = "fea3f!cd";
    String nickname = "민하";
    String email = "minah@jy.com";

    @Test
    @Transactional
    public void 임시비밀번호_테스트() {

        // given
        String exPassword = passwordEncoder.encode(rawPassword);

        Member member = Member.builder()
                .username(username)
                .password(exPassword)
                .nickname(nickname)
                .email(email)
                .role(Role.USER)
                .build();

        memberRepository.save(member);

        // when
        /* 임시 비밀번호 반환 */
        String tmpPassword = memberService.getTmpPassword();
        /* 임시 비밀번호로 업데이트 */
        memberService.updatePassword(tmpPassword, member.getEmail());

        // then
        /* 이전 비밀번호와 임시 비밀번호로 바뀐 비밀번호가 다른지 확인 */
        assertThat(exPassword).isNotEqualTo(member.getPassword());

        log.info("테스트 성공");

    }
}
