package travelmate.backend.dto;

import travelmate.backend.entity.Member;

public record TourSpotReviewWriterDto(
        String username,
        String nickname,
        String profileImageUrl
) {
    public TourSpotReviewWriterDto(Member member){
        this(
                member.getUsername(),
                member.getNickname(),
                member.getProfileImage()
        );
    }
}
