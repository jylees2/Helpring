package com.jy.helpring.service.mail;

import com.jy.helpring.web.vo.MailVo;

public interface MailService {

    /** 이메일 생성 **/
    MailVo createMail(String tmpPassword, String memberEmail);

    /** 이메일 전송 **/
    void sendMail(MailVo mailVo);
}
