package travelmate.backend.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelmate.backend.dto.CustomOAuth2User;
import travelmate.backend.jwt.JWTUtil;
import travelmate.backend.repository.UserRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    @Transactional
    public void deleteUserDB(String username) {
        userRepository.deleteByUsername(username);
    }

    public void deleteAuthenticationObject(HttpServletRequest request, HttpServletResponse response) {

        // SecurityContextHolder 에서 인증 확인 후 인증 객체 불러옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그아웃 핸들러에서 (세션 무효화), (쿠키 삭제), 인증 객체 삭제
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void deleteToken(HttpServletRequest request, HttpServletResponse response){

        // 토큰 check
        String access = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                access = cookie.getValue();
            }
        }

        // 토큰 null check
        if (access == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰 만료 check
        try {
            jwtUtil.isExpired(access);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰 Cookie 값 0 으로 설정
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public int requestKakaoServiceByAdminKey(String kakaoApiUrl, String appAdminKey, String providerId) throws IOException {

        // URL 객체 생성
        URL url = new URL(kakaoApiUrl);
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        // 헤더 설정
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization", "KakaoAK " + appAdminKey);

        // POST 데이터 설정
        String postData = "target_id_type=user_id&target_id=" + providerId;
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(postData.getBytes());
            os.flush();
        }

        // 응답 코드 확인
        return conn.getResponseCode();
    }

}
