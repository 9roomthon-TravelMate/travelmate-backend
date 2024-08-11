package travelmate.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import travelmate.backend.dto.*;
import travelmate.backend.projection.TourSpotReviewAggregation;
import travelmate.backend.service.TourSpotReviewService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TourSpotReviewController {

    private final TourSpotReviewService tourSpotReviewService;

    @GetMapping("/tourspots/{tourSpotId}/reviews")
    public TourSpotReviewListDto getReviewList(@PathVariable Long tourSpotId,
                                            @ModelAttribute TourSpotReviewQueryRequest query) {
        Page<TourSpotReviewDto> page = tourSpotReviewService.findTourSpotReviewsByQuery(tourSpotId, query);
        TourSpotReviewAggregation aggregation = tourSpotReviewService.aggregateByTourSpot(tourSpotId);
        return new TourSpotReviewListDto(page, aggregation);
    }

    @GetMapping("/tourspots/{tourSpotId}/reviews/my")
    public TourSpotReviewDto getMyReview(@PathVariable Long tourSpotId,
                                         @AuthenticationPrincipal CustomOAuth2User user) {
        TourSpotReviewDto review = tourSpotReviewService.findByTourSpotAndUser(tourSpotId, user.getUsername());
        return review;
    }

    @PostMapping("/tourspots/{tourSpotId}/reviews")
    public TourSpotReviewDto writeReview(@PathVariable Long tourSpotId,
                                         @Valid @RequestBody TourSpotReviewCreateRequest request,
                                         @AuthenticationPrincipal CustomOAuth2User user) {
        TourSpotReviewDto review = tourSpotReviewService.createReview(tourSpotId, request, user.getUsername());
        return review;
    }

    @PutMapping("/tourspot/reviews/{reviewId}")
    public TourSpotReviewDto updateReview(@PathVariable Long reviewId,
                                          @RequestBody TourSpotReviewUpdateRequest request,
                                          @AuthenticationPrincipal CustomOAuth2User user) {
        TourSpotReviewDto review = tourSpotReviewService.updateReview(reviewId, request, user.getUsername());
        return review;
    }

    @DeleteMapping("/tourspot/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId,
                                               @AuthenticationPrincipal CustomOAuth2User user) {
        tourSpotReviewService.deleteReview(reviewId, user.getUsername());
        return ResponseEntity.ok("success");
    }
}
