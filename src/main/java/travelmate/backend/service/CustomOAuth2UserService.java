package travelmate.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import travelmate.backend.dto.CustomOAuth2User;
import travelmate.backend.dto.KakaoResponse;
import travelmate.backend.dto.OAuth2Response;
import travelmate.backend.dto.UserDTO;
import travelmate.backend.entity.Member;
import travelmate.backend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. OAuth2 로그인 유저 정보를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        // 2. 어떤 provider 인지 (provider : kakao, naver, google ...)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. 필요한 정보를 provider에 따라 다르게 mapping
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        // 4. 소셜 서비스에서 제공한 사용자 정보로 해당 서비스 사용자를 특성할 아이디 값 생성
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        Member isExist = userRepository.findByUsername(username);
        String role = null;

        // 5. DB에 사용자 정보 저장
        if (isExist == null) {
            Member member = new Member();
            member.setUsername(username);
            member.setNickname(oAuth2Response.getName());
            member.setProfileImage(oAuth2Response.getProfile());
            member.setRole("ROLE_USER");
            userRepository.save(member);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setNickname(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");

            // 6. OAuth2LoginAuthenticationProvider는 OAuth2User 형식에 맞는 값을 받아 로그인을 진행
            return new CustomOAuth2User(userDTO);

        } else {
            role = isExist.getRole();
            isExist.setNickname(oAuth2Response.getName());
            isExist.setProfileImage(oAuth2Response.getProfile());
            userRepository.save(isExist);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(isExist.getUsername());
            userDTO.setNickname(oAuth2Response.getName());
            userDTO.setRole(isExist.getRole());

            // 6. OAuth2LoginAuthenticationProvider는 OAuth2User 형식에 맞는 값을 받아 로그인을 진행
            return new CustomOAuth2User(userDTO);
        }

    }
}
