package com.jy.helpring.web.dto.member;

import com.jy.helpring.domain.Role;
import com.jy.helpring.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class MemberDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto{

        private Long id;

        @NotBlank(message = "아이디는 필수 입력값입니다.")
        @Pattern(regexp = "")
        private String username;

        @Pattern(regexp = "")
        private String password;

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        @Pattern(regexp = "")
        private String nickname;

        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Pattern(regexp = "")
        private String email;

        private Role role;

        /** 암호화된 password **/
        public void encryptPassword(String BCryptpassword){
            this.password = BCryptpassword;
        }

        /** Dto -> Entity **/
        public Member toEntity(){
            Member member = Member.builder()
                    .id(id)
                    .username(username)
                    .password(password)
                    .nickname(nickname)
                    .email(email)
                    .role(role.USER)
                    .build();
            return member;
        }
    }

    /**
     * 인증된 사용자 정보를 세션에 저장하기 위한 클래스
     * 세션에 저장하기 위해 직렬화
     */

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto implements Serializable{
        private Long id;
        private String username;
        private String nickname;
        private String email;
        private Role role;
        private String updatedDate;

        /* Entity -> DTO */
        public ResponseDto(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.nickname = member.getNickname();
            this.email = member.getEmail();
            this.role = member.getRole();
            this.updatedDate = member.getUpdatedDate();
        }
    }
}
