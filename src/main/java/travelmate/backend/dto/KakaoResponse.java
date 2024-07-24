package travelmate.backend.dto;

import lombok.RequiredArgsConstructor;
import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private Map<String, Object> attribute;
    private Map<String, Object> attributeAccount;
    private Map<String, Object> attributeProfile;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
        this.attributeAccount = (Map<String, Object>) attribute.get("kakao_account");
        this.attributeProfile = (Map<String, Object>) attributeAccount.get("profile");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getName() {
        return (String) attributeProfile.get("nickname");
    }

    @Override
    public String getProfile() {
        return (String) attributeProfile.get("profile_image_url");
    }
}
