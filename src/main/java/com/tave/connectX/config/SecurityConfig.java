package com.tave.connectX.config;

import com.tave.connectX.provider.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtProvider jwtProvider;

    @Autowired
    public SecurityConfig(@Lazy JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ID, Password 문자열을 Base64로 인코딩하여 전달하는 구조
                .httpBasic().disable()
                // 쿠키 기반이 아닌 JWT 기반이므로 사용하지 않음
                .csrf().disable()
                // Spring Security 세션 정책 : 세션을 생성 및 사용하지 않음
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests()
                // 로그인은 모두 승인
                .requestMatchers( "/login/**", "/oauth2/**","v3/api-docs/**", "/swagger-resources/", "/swagger-ui/**",
                        "/webjars/", "/swagger/**").permitAll()
                //admin으로 시작하는 요청은 ADMIN 권한이 있는 유저에게만 허용
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and()
                // JWT 인증 필터 적용
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                // 에러 핸들링
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    // 권한 문제가 발생했을 때 이 부분을 호출한다.
                    response.setStatus(403);
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write("권한이 없는 사용자입니다.");
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    // 인증문제가 발생했을 때 이 부분을 호출한다.
                    response.setStatus(401);
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write("인증되지 않은 사용자입니다.");
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
