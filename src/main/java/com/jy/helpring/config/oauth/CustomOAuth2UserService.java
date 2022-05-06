package com.jy.helpring.config.oauth;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.config.oauth.provider.GoogleUserInfo;
import com.jy.helpring.config.oauth.provider.NaverUserInfo;
import com.jy.helpring.config.oauth.provider.OAuth2UserInfo;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /** OAuth2 서비스 ID 구분 코드 - 구글, 네이버 **/
        OAuth2UserInfo oAuth2UserInfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            log.info("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
            // JSON 형태이므로 Map을 통해 데이터를 가져옴
        }

        /** 소셜 로그인 시 기존 회원이 존재하는 경우 바뀐 정보 업데이트 **/
        Member member = saveOrUpdate(oAuth2UserInfo);

        return new UserAdapter(member, oAuth2User.getAttributes());

    }

    /** 소셜 로그인 시 기존 회원이 존재하는 경우 수정 날짜만 업데이트 **/
    private Member saveOrUpdate(OAuth2UserInfo userInfo) {
        /* 사용자 반환하여 수정 날짜 업데이트 */
        Member member = memberRepository.findByEmail(userInfo.getEmail())
                .map(entity -> entity.updateUpdatedDate())
                .orElse(userInfo.toEntity());

        log.info("username : " + member.getUsername());
        log.info("nickname : " + member.getNickname());
        log.info("createdDate : " + member.getCreatedDate());
        log.info("role : "+member.getRole());

        return memberRepository.save(member);
    }
}
