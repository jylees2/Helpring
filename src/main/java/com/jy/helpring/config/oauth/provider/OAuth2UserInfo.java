package com.jy.helpring.config.oauth.provider;

import com.jy.helpring.domain.member.Member;

import java.util.Map;

public interface OAuth2UserInfo {
    Map<String, Object> getAttributes();
    String getNameAttributeKey();
    String getUsername();
    String getNickname();
    String getEmail();

    Member toEntity();
}
