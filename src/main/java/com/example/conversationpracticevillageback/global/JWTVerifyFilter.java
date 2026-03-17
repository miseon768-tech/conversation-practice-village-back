package com.example.conversationpracticevillageback.global;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class JWTVerifyFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    // JWT 필터 제외 경로 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        return
                // Swagger 허용
                uri.startsWith("/swagger-ui") ||
                        uri.startsWith("/v3/api-docs") ||
                        uri.equals("/swagger-ui.html") ||

                        // 인증 없이 허용 (복수형 members 경로 추가)
                        uri.equals("/api/members/login") ||
                        uri.equals("/api/members/signup") ||

                        uri.equals("/api/members/email/code") ||
                        uri.equals("/api/members/social") ||
                        uri.equals("/api/members/email/verify") ||
                        uri.equals("/api/auth/google") ||
                        uri.equals("/api/auth/naver") ||

                        method.equals("OPTIONS") ||
                        uri.startsWith("/stomp") ||
                        uri.startsWith("/ws") ||
                        uri.startsWith("/ws-stomp") ||
                        uri.startsWith("/ws-endpoint") ||
                        uri.startsWith("/sockjs") ||
                        uri.startsWith("/socket");
    }

    // 실제 JWT 검증 로직
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"message\":\"토큰이 없거나 형식이 틀립니다.\"}");
            return;
        }

        String token = authHeader.substring(7);
        DecodedJWT jwt;

        try {
            jwt = jwtUtil.verifyToken(token);
        } catch (JWTVerificationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"message\":\"사용자 토큰이 일치하지 않습니다.\"}");
            return;
        }

        String memberId = jwt.getSubject();

        Member member;
        try {
            member = memberRepository.findById(Long.valueOf(memberId))
                    .orElseThrow(() -> new NoSuchElementException("사용자 정보가 없습니다."));
        } catch (NoSuchElementException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"message\":\"사용자 정보가 없습니다.\"}");
            return;
        }

        // ★ 여기서 SecurityContext에 인증 정보 심기 ★
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        member,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 컨트롤러에서 @RequestAttribute로 member 사용 가능
        request.setAttribute("member", member);

        filterChain.doFilter(request, response);
    }
}