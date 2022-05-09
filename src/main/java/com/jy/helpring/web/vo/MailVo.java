package com.jy.helpring.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 메일 메시지 정보 **/

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailVo {
    private String toAddress; // 받는 이메일 주소
    private String title; // 이메일 제목
    private String message; // 이메일 내용
    private String fromAddress; // 보내는 이메일 주소

}
