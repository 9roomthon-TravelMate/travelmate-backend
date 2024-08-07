package travelmate.backend.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import travelmate.backend.dto.CustomOAuth2User;
import travelmate.backend.entity.Member;
import travelmate.backend.jwt.JWTUtil;
import travelmate.backend.repository.UserRepository;
import travelmate.backend.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${spring.security.oauth2.client.registration.kakao.admin-key}")
    private String appAdminKey;

    @GetMapping("/users")
    public ResponseEntity<?> getUserInfo (@AuthenticationPrincipal CustomOAuth2User user){

        HashMap<Object, Object> userInfo = new HashMap<>();
        userInfo.put("nickname", user.getName());
        userInfo.put("profile_image", user.getProfileImage());

        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }

    @PostMapping("/users/delete")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomOAuth2User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String kakaoApiUrl = "https://kapi.kakao.com/v1/user/unlink";

        // 1. 카카오 연결끊기
        String providerId = getProviderId(user);
        System.out.println(providerId);
        int responseCode = userService.requestKakaoServiceByAdminKey(kakaoApiUrl, appAdminKey, providerId);
        if (responseCode == 200) {
            System.out.println("카카오 연결끊기 성공");
        } else {
            throw new RuntimeException("카카오 연결끊기 실패 : " + responseCode);

        }

        // 2. 인증 객체 삭제
        userService.deleteAuthenticationObject(request, response);

        // 3. DB 삭제
        userService.deleteUserDB(user.getUsername());

        // 4. 토큰 삭제
        userService.deleteToken(request, response);

        return ResponseEntity.status(HttpStatus.OK).body("서비스 회원탈퇴 성공");
    }

    private String getProviderId(CustomOAuth2User user){
        String[] parts = user.getUsername().split(" ");
        return parts[1];
    }

}
