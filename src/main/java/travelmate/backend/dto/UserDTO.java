package travelmate.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDTO {

    private String username;
    private String nickname;
    private String profile_image;
    private String role;

}
