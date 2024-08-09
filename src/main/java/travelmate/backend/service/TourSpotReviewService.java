package travelmate.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelmate.backend.dto.*;
import travelmate.backend.entity.Member;
import travelmate.backend.entity.TourSpot;
import travelmate.backend.entity.TourSpotReview;
import travelmate.backend.projection.TourSpotReviewAggregation;
import travelmate.backend.repository.TourSpotRepository;
import travelmate.backend.repository.TourSpotReviewRepository;
import travelmate.backend.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TourSpotReviewService {

    private final TourSpotReviewRepository tourSpotReviewRepository;
    private final TourSpotRepository tourSpotRepository;
    private final UserRepository userRepository;

    @Transactional
    public TourSpotReviewDto createReview(Long tourSpotId, TourSpotReviewCreateRequest request, String username) {
        TourSpot tourSpot = tourSpotRepository.findById(tourSpotId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관광지 입니다."));

        Member member = userRepository.findByUsername(username);
        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        }

        Optional<TourSpotReview> existingReview = tourSpotReviewRepository.findByTourSpotIdAndMemberId(tourSpot.getId(), member.getId());
        if (existingReview.isPresent()) {
            throw new IllegalStateException("해당 관광지에 이미 작성된 후기가 있습니다.");
        }

        TourSpotReview review = TourSpotReview.builder()
                .rating(request.rating())
                .text(request.text())
                .tourSpot(tourSpot)
                .member(member)
                .build();

        tourSpotReviewRepository.save(review);

        return new TourSpotReviewDto(review, tourSpot, member);
    }

    @Transactional
    public TourSpotReviewDto updateReview(Long reviewId, TourSpotReviewUpdateRequest request, String username) {
        TourSpotReview review = tourSpotReviewRepository.findByIdAndMemberUsername(reviewId, username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관광지 리뷰이거나 작성자가 아닙니다."));

        review.update(request.rating(), request.text());

        tourSpotReviewRepository.save(review);

        return new TourSpotReviewDto(review, review.getTourSpot(), review.getMember());
    }

    @Transactional
    public void deleteReview(Long reviewId, String username) {
        TourSpotReview review = tourSpotReviewRepository.findByIdAndMemberUsername(reviewId, username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관광지 리뷰이거나 작성자가 아닙니다."));

        tourSpotReviewRepository.delete(review);
    }

    public Page<TourSpotReviewDto> findTourSpotReviewsByQuery(Long tourSpotId, TourSpotReviewQueryRequest query) {
        TourSpot tourSpot = tourSpotRepository.findById(tourSpotId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관광지 입니다."));

        Integer pageNumber = query.pageNumber();
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }

        Integer pageSize = query.pageSize();
        if (pageSize == null || pageSize < 10) {
            pageSize = 10;
        } else if (pageSize > 1000) {
            pageSize = 1000;
        }

        Pageable pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());

        Page<TourSpotReview> reviewPage = tourSpotReviewRepository.findByFilter(tourSpotId, query.rating(), pageRequest);

        return reviewPage.map(review -> new TourSpotReviewDto(review, tourSpot, review.getMember()));
    }

    public TourSpotReviewAggregation aggregateByTourSpot(Long tourSpotId) {
        return tourSpotReviewRepository.aggregateByTourSpotId(tourSpotId);
    }

    public TourSpotReviewDto findByTourSpotAndUser(Long tourSpotId, String username) {

        TourSpotReview review = tourSpotReviewRepository.findByTourSpotIdAndMemberUsername(tourSpotId, username)
                .orElseThrow(() -> new IllegalArgumentException("작성된 리뷰가 없습니다."));

        return new TourSpotReviewDto(review, review.getTourSpot(), review.getMember());
    }

}
