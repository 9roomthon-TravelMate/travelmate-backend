package travelmate.backend.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import travelmate.backend.dto.CustomOAuth2User;
import travelmate.backend.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilter {

    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final String appAdminKey;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();

        if (!requestURI.matches("^\\/api/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        String access = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                access = cookie.getValue();
            }
        }

        // 토큰 null & 만료 check
        if (access == null || jwtUtil.isExpired(access)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.getUsername(access);
        String[] parts = username.split(" ");
        String providerId = parts[1];

        // 1. 카카오 로그아웃
        String kakaoApiUrl = "https://kapi.kakao.com/v1/user/logout";

        int responseCode = userService.requestKakaoServiceByAdminKey(kakaoApiUrl, appAdminKey, providerId);
        if (responseCode == 200) {
            System.out.println("카카오 로그아웃 성공");
        } else {
            throw new RuntimeException("카카오 로그아웃 실패 : " + responseCode);

        }

        // 2. 토큰 삭제
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
