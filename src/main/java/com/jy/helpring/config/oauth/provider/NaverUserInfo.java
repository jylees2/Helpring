package com.jy.helpring.config.oauth.provider;

import com.jy.helpring.domain.Role;
import com.jy.helpring.domain.member.Member;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

    private Map<String, Object> response;

    /*
    { id = ...
            email = ...
            name = ...
    }
     */

    /** 생성자 **/
    public NaverUserInfo(Map<String, Object> attributes){
        this.response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return response;
    }

    @Override
    public String getNameAttributeKey() {
        return "id";
    }

    @Override
    public String getUsername() {
        return (String)response.get("email");
    }

    @Override
    public String getNickname() {
        return (String)response.get("name");
    }

    @Override
    public String getEmail() {
        return (String)response.get("email");
    }

    @Override
    public Member toEntity() {
        return Member.builder()
                .username(getUsername())
                .nickname(getNickname())
                .email(getEmail())
                .role(Role.SOCIAL)
                .build();
    }
}
