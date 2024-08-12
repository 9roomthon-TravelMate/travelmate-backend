package travelmate.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import travelmate.backend.dto.*;
import travelmate.backend.dto.tourApi.item.TourApiTourCommonDetail;
import travelmate.backend.dto.tourApi.item.TourApiTourImage;
import travelmate.backend.entity.TourRegion;
import travelmate.backend.entity.TourSpot;
import travelmate.backend.entity.TourSpotDetail;
import travelmate.backend.entity.TourSpotImage;
import travelmate.backend.projection.TourSpotReviewAggregation;
import travelmate.backend.repository.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TourSpotService {

    private final TourApiService tourApiService;

    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;
    private final TourSpotRepository tourSpotRepository;
    private final TourSpotThemeRepository tourSpotThemeRepository;
    private final TourSpotDetailRepository tourSpotDetailRepository;
    private final TourSpotImageRepository tourSpotImageRepository;
    private final TourSpotReviewRepository tourSpotReviewRepository;

    public List<TourRegionDto> findAllRegions() {
        return regionRepository.findAll()
                .stream()
                .map(TourRegionDto::new)
                .toList();
    }

    public List<TourDistrictDto> findAllDistricts() {
        return districtRepository.findAll()
                .stream()
                .map(district -> new TourDistrictDto(district, district.getRegion()))
                .toList();
    }

    public List<TourDistrictDto> findDistrictsByRegionId(Long regionId) {
        TourRegion region = regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역입니다."));

        return region.getDistricts()
                .stream()
                .map(district -> new TourDistrictDto(district, region))
                .toList();
    }

    public List<TourSpotThemeDto> findAllTourSpotThemes() {
        return tourSpotThemeRepository.findAll()
                .stream()
                .map(TourSpotThemeDto::new)
                .toList();
    }

    public Page<TourSpotSummaryDto> findTourSpotsByQuery(TourSpotQueryRequest query) {
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

        Pageable pageRequest = PageRequest.of(pageNumber - 1, pageSize);

        return tourSpotRepository.findByFilter(
                query.regionId(),
                query.districtId(),
                query.themeId(),
                pageRequest);
    }

    @Transactional
    public TourSpotDetailsDto findTourSpotDetailById(Long tourSpotId) {
        TourSpot tourSpot = tourSpotRepository.findById(tourSpotId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관광지입니다."));

        String contentId = tourSpot.getContentId();

        Mono<Optional<TourApiTourCommonDetail>> detailMono = tourSpot.getDetail() == null ?
                tourApiService.fetchTourSpotDetail(contentId).map(Optional::ofNullable).defaultIfEmpty(Optional.empty()) :
                Mono.just(Optional.empty());

        Mono<Optional<List<TourApiTourImage>>> imagesMono = tourSpot.getImagesUpdatedAt() == null ?
                tourApiService.fetchTourSpotImages(contentId).map(Optional::ofNullable).defaultIfEmpty(Optional.empty()) :
                Mono.just(Optional.empty());

        Tuple2<Optional<TourApiTourCommonDetail>, Optional<List<TourApiTourImage>>> zip = Mono.zip(
                detailMono,
                imagesMono
        ).block();

        final boolean isTourSpotUpdated = tourSpot.getDetail() == null || tourSpot.getImagesUpdatedAt() == null;

        if (tourSpot.getDetail() == null) {
            Optional<TourApiTourCommonDetail> commonDetail = zip.getT1();
            TourSpotDetail tourSpotDetail = commonDetail.map(TourSpotDetail::new).orElseGet(TourSpotDetail::new);
            tourSpotDetailRepository.save(tourSpotDetail);
            tourSpot.updateDetail(tourSpotDetail);
        }

        if (tourSpot.getImagesUpdatedAt() == null) {
            Optional<List<TourApiTourImage>> images = zip.getT2();
            if (images.isPresent()) {
                List<TourSpotImage> list = images.get().stream()
                        .map(image -> new TourSpotImage(image, tourSpot)).toList();
                tourSpotImageRepository.saveAll(list);
            }
            tourSpot.updateImagesUpdatedAt();
        }

        if (isTourSpotUpdated) {
            tourSpotRepository.save(tourSpot);
        }

        List<TourSpotImageDto> tourSpotImageDtoList = tourSpot.getImages().stream()
                .map(TourSpotImageDto::new)
                .toList();

        TourSpotReviewAggregation reviewAggregation = tourSpotReviewRepository.aggregateByTourSpotId(tourSpotId);

        return new TourSpotDetailsDto(tourSpot, tourSpot.getDetail(), tourSpotImageDtoList, reviewAggregation);
    }
}
