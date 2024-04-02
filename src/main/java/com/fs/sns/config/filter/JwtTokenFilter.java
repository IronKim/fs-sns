package com.fs.sns.config.filter;

import com.fs.sns.model.User;
import com.fs.sns.service.UserService;
import com.fs.sns.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;

    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/v1/users/alarm/subscribe");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // get header
        final String token;
        try {
            if(TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())) {
                log.info("Request with {} check the query param", request.getRequestURI());
                token = request.getQueryString().split("=")[1].trim();
            } else {
                final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
                if(header == null || !header.startsWith("Bearer ")) {
                    log.error("Error occurs while getting header. header is null or invalid");
                    filterChain.doFilter(request, response);
                    return;
                }
                token = header.split(" ")[1].trim();
            }

            if (JwtTokenUtils.isExpired(token, key)) {
                log.error("Key is expired");
                filterChain.doFilter(request, response);
                return;
            }

            String username = JwtTokenUtils.getUserName(token, key);
            User user = userService.loadUserByUserName(username);


            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities() // 사용자 정보, 패스워드, 권한 목록을 파라미터로 받아 UsernamePasswordAuthenticationToken 객체를 생성한다.
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // WebAuthenticationDetailsSource를 통해 HttpServletRequest 객체를 이용해 WebAuthenticationDetails 객체를 생성한다.
            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder에 인증 정보를 저장한다. 이후 필요한 곳에서 SecurityContextHolder를 통해 인증 정보를 가져올 수 있다.
        }catch (RuntimeException e) {
            log.error("Error occurs while validating. {}", e.toString());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
