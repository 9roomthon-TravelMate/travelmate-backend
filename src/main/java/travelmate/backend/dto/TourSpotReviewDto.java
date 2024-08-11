package travelmate.backend.dto;

import travelmate.backend.entity.Member;
import travelmate.backend.entity.TourSpot;
import travelmate.backend.entity.TourSpotReview;

import java.util.Optional;

public record TourSpotReviewDto(
        Long id,
        Long tourSpotId,
        Integer rating,
        String text,
        TourSpotReviewWriterDto writer,
        Long createdAt,
        Long updatedAt // 업데이트 결과 응답에서는 이전 값을 갖기 때문에 사용하면 안됨. (쓰기 지연)
) {
    public TourSpotReviewDto(TourSpotReview review, TourSpot tourSpot, Member member) {
        this(
                review.getId(),
                tourSpot.getId(),
                review.getRating(),
                review.getText(),
                Optional.ofNullable(member).map(TourSpotReviewWriterDto::new).orElse(null),
                review.getCreatedAt().toEpochMilli(),
                review.getUpdatedAt().toEpochMilli()
        );
    }
}
