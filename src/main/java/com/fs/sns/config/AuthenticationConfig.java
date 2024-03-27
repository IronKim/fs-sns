package com.fs.sns.config;

import com.fs.sns.config.filter.JwtTokenFilter;
import com.fs.sns.exception.CustomAuthenticationEntryPoint;
import com.fs.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserService userService;
    @Value("${jwt.secret-key}")
    private String key;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않겠다는 설정
                .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class) // JwtTokenFilter를 UsernamePasswordAuthenticationFilter 앞에 추가한다. UsernamePasswordAuthenticationFilter는
                .exceptionHandling(authenticationManager -> authenticationManager.authenticationEntryPoint(new CustomAuthenticationEntryPoint())); // 인증되지 않은 사용자가 리소스에 액세스하려고 할 때 호출되는 커스텀 핸들러

        return http.build();
    }

}
