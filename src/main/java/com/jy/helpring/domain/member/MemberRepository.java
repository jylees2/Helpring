package com.jy.helpring.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /** Security **/
    Optional<Member> findByUsername(String username);

    Member findByNickname(String nickname);

    /**
     * 유효성 검사 - 중복 체크
     * 중복 : true
     * 중복이 아닌 경우 : false
     */
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);

    /** OAuth 로그인 시 중복 체크 **/
    Optional<Member> findByEmail(String email);
}
