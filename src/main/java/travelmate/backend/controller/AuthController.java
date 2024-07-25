package travelmate.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import travelmate.backend.dto.CustomOAuth2User;
import travelmate.backend.entity.Member;
import travelmate.backend.repository.UserRepository;

import java.util.HashMap;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    // controller 하나 쓰므로 임시로 repository 직접 사용함
    private final UserRepository userRepository;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo (){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User customUserDetails = (CustomOAuth2User) principal;
            String username = customUserDetails.getUsername();

            Member member = userRepository.findByUsername(username);
            if (member == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자를 찾을 수 없습니다.");
            }

            HashMap<Object, Object> userInfo = new HashMap<>();
            userInfo.put("nickname", member.getNickname());
            userInfo.put("profile_image", member.getProfileImage());

            return ResponseEntity.ok(userInfo);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
    }
}
