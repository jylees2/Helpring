package com.jy.helpring.config;

import com.jy.helpring.config.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebConfig implements WebMvcConfigurer {

    private final CustomUserDetails customUserDetails;
}
