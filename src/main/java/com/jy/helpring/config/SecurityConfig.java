package com.jy.helpring.config;

import com.jy.helpring.config.auth.CustomUserDetailsService;
import com.jy.helpring.config.oauth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;

    /** 로그인 실패 핸들러 의존성 주입 **/
    private final AuthenticationFailureHandler customFailureHandler;

    /** OAuth **/
    private final CustomOAuth2UserService customOAuth2UserService;

    /** BCryptPasswordEncoder Encoder() 빈 등록 **/
    @Bean
    public BCryptPasswordEncoder Encoder(){
        return new BCryptPasswordEncoder();
    }

    /** AuthenticationManager 빈 등록 **/
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /** UsernameNotFoundException이 작동하지 않는 문제 **/
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(Encoder());
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return authenticationProvider;
    }

    /** 시큐리티 로그인 과정에서 시큐리티가 암호를 가로챌 때 어떤 해쉬로 암호화했는지 확인 **/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /** static 관련 인증 설정 무시 **/
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.
                ignoring().antMatchers("/assets/**", "/error", "/post_upload/**", "/lecture_upload/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().ignoringAntMatchers("/rest/**") /* REST API 사용 시 csrf 예외 처리 */
                .and()
                .authorizeRequests()
                .antMatchers("/", "/index", "/lecture/**", "/community/post/**", "/auth/**",
                        "/sendPwd/**", "/rest/checkEmail/**","/rest/sendPwd/**",
                        "/v2/**", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
                .antMatchers("/admin/**").access("hasRole('ADMIN')")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/login") /* 권한이 필요한 어느 페이지에 접근하든 무조건 /login 페이지로 이동 */
                .loginProcessingUrl("/auth/loginProc") /* 시큐리티에서 해당 주소로 오는 요청을 낚아채 수행 */
                .failureHandler(customFailureHandler) /* 로그인 실패 핸들러*/
                .defaultSuccessUrl("/") /* 로그인 성공 시 이동되는 페이지 */
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) /* 로그아웃 경로 */
                .invalidateHttpSession(true).deleteCookies("JSESSIONID") /* 로그아웃 성공 시 세션 제거 */
                .logoutSuccessUrl("/") /* 로그아웃 성공 후 이동 경로 */
                .and()
                .oauth2Login()
                .userInfoEndpoint() /* OAuth 2 로그인 성공 후 사용자 정보를 가져올 때의 설정 담당 */
                .userService(customOAuth2UserService); /* 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스 구현체 등록, 사용자 정보를 가져온 상태에서 추가 진행 기능 */
    }
}
