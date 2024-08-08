package travelmate.backend.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import travelmate.backend.client.tourApi.TourApiEnglishInfoClient;
import travelmate.backend.client.tourApi.TourApiKoreanInfoClient;
import travelmate.backend.dto.tourApi.item.TourApiAreaInfo;
import travelmate.backend.dto.tourApi.item.TourApiTourCommonDetail;
import travelmate.backend.dto.tourApi.item.TourApiTourImage;
import travelmate.backend.dto.tourApi.item.TourApiTourInfo;
import travelmate.backend.entity.*;
import travelmate.backend.repository.DistrictRepository;
import travelmate.backend.repository.RegionRepository;
import travelmate.backend.repository.TourSpotRepository;
import travelmate.backend.repository.TourSpotThemeRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class TourApiService {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    private final TourApiKoreanInfoClient koreanInfoClient;
    private final TourApiEnglishInfoClient englishInfoClient;

    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;
    private final TourSpotThemeRepository tourSpotThemeRepository;
    private final TourSpotRepository tourSpotRepository;

    @Scheduled(cron = "0 30 4 * * ?")
    public void updateTourSpotList() {
        fetchAndUpsertRegions();
        fetchAndUpsertAllDistricts();
        fetchAndUpsertAllTourSpots();
    }

    private void insertDefaultThemeDetails() {
        List<TourSpotThemeDetail> themeDetails = Arrays.stream(TourSpotTheme.values())
                .map(theme -> new TourSpotThemeDetail(theme, theme.getDefaultTitle()))
                .toList();
        tourSpotThemeRepository.saveAll(themeDetails);
    }

    @PostConstruct
    private void init() {
        if (activeProfile.equals("local") && tourSpotThemeRepository.count() == 0) {
            insertDefaultThemeDetails();
        }
        if (regionRepository.count() == 0 || districtRepository.count() == 0 || tourSpotRepository.count() == 0) {
            updateTourSpotList();
        }
    }

    @Transactional
    public void fetchAndUpsertRegions() {
        Tuple2<List<TourApiAreaInfo>, List<TourApiAreaInfo>> lists = Mono.zip(
                koreanInfoClient.getRegionInfoList(),
                englishInfoClient.getRegionInfoList()
        ).block();

        List<TourApiAreaInfo> koreanRegionInfoList = lists.getT1();

        Map<String, String> englishRegionNameByCode = lists.getT2().stream()
                .collect(Collectors.toMap(TourApiAreaInfo::code, TourApiAreaInfo::name));

        Map<String, TourRegion> existingRegionByCode = regionRepository.findAll().stream()
                .collect(Collectors.toMap(TourRegion::getCode, Function.identity()));

        List<TourRegion> regions = koreanRegionInfoList.stream()
                .map(koreanRegionInfo -> {
                    String code = koreanRegionInfo.code();
                    String koreanName = koreanRegionInfo.name();
                    String englishName = englishRegionNameByCode.get(code);

                    TourRegion region = existingRegionByCode.get(code);
                    if (region == null) {
                        return new TourRegion(code, koreanName, englishName);
                    } else {
                        region.updateNames(koreanName, englishName);
                        return region;
                    }
                })
                .toList();

        // TODO: koreanRegionInfoList의 code들에 매치되지 않는 existingRegions에 대한 비활성화 처리

        regionRepository.saveAll(regions);
    }

    @Transactional

    private void fetchAndUpsertDistricts(TourRegion region) {
        Tuple2<List<TourApiAreaInfo>, List<TourApiAreaInfo>> lists = Mono.zip(
                koreanInfoClient.getDistrictInfoList(region.getCode()),
                englishInfoClient.getDistrictInfoList(region.getCode())
        ).block();

        List<TourApiAreaInfo> koreanDistrictInfoList = lists.getT1();

        Map<String, String> englishDistrictNameByCode = lists.getT2().stream()
                .collect(Collectors.toMap(TourApiAreaInfo::code, TourApiAreaInfo::name));

        List<TourDistrict> existingDistricts = districtRepository.findByRegionId(region.getId());

        Map<String, TourDistrict> existingDistrictByCode = existingDistricts.stream()
                .collect(Collectors.toMap(TourDistrict::getCode, Function.identity()));

        List<TourDistrict> districts = koreanDistrictInfoList.stream()
                .map(koreanDistrictInfo -> {
                    String code = koreanDistrictInfo.code();
                    String koreanName = koreanDistrictInfo.name();
                    String englishName = englishDistrictNameByCode.get(code);

                    TourDistrict district = existingDistrictByCode.get(code);
                    if (district == null) {
                        return new TourDistrict(region, code, koreanName, englishName);
                    } else {
                        district.updateNames(koreanName, englishName);
                        return district;
                    }
                })
                .toList();

        // TODO: koreanDistrictInfoList의 code들에 매치되지 않는 existingDistricts에 대한 비활성화 처리

        districtRepository.saveAll(districts);
    }

    public void fetchAndUpsertAllDistricts() {
        List<TourRegion> regions = regionRepository.findAll();
        regions.forEach(this::fetchAndUpsertDistricts);
    }

    private String createDistrictKey(Long regionId, String code) {
        return regionId + "_" + code;
    }

    @Transactional
    public void upsertTourSpots(List<TourApiTourInfo> tourInfoList) {
        EnumMap<TourSpotTheme, TourSpotThemeDetail> themeDetailByTheme = tourSpotThemeRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        TourSpotThemeDetail::getTheme,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        () -> new EnumMap<>(TourSpotTheme.class)
                ));

        for (TourSpotTheme theme : TourSpotTheme.values()) {
            if (!themeDetailByTheme.containsKey(theme)) {
                throw new IllegalArgumentException("Missing tour spot theme: " + theme);
            }
        }

        Map<String, TourRegion> regionByCode = regionRepository.findAll().stream()
                .collect(Collectors.toMap(TourRegion::getCode, Function.identity()));

        Map<String, TourDistrict> districtByKey = districtRepository.findAll().stream()
                .collect(Collectors.toMap(
                        district -> createDistrictKey(district.getRegion().getId(), district.getCode()),
                        Function.identity()
                ));

        List<String> contentIds = tourInfoList.stream().map(TourApiTourInfo::contentid).toList();

        Map<String, TourSpot> existingTourSpotByContentId = tourSpotRepository.findAllByContentIdIn(contentIds).stream()
                .collect(Collectors.toMap(TourSpot::getContentId, Function.identity()));


        List<TourSpot> tourSpots = tourInfoList.stream().map(tourInfo -> {
            TourRegion region = regionByCode.get(tourInfo.areacode());
            TourDistrict district = null;

            if (region != null) {
                String districtKey = createDistrictKey(region.getId(), tourInfo.sigungucode());
                district = districtByKey.get(districtKey);
            }

            TourSpotTheme tourSpotTheme = tourInfo.getTourSpotTheme();
            TourSpotThemeDetail tourSpotThemeDetail = themeDetailByTheme.get(tourSpotTheme);

            TourSpot tourSpot = existingTourSpotByContentId.get(tourInfo.contentid());
            if (tourSpot == null) {
                return new TourSpot(tourInfo, region, district, tourSpotThemeDetail);
            } else {
                tourSpot.update(tourInfo, region, district, tourSpotThemeDetail);
                return tourSpot;
            }
        }).toList();

        // TODO: tourInfoList의 contentid들에 매치되지 않는 existingTourSpots에 대한 비활성화 처리

        tourSpotRepository.saveAll(tourSpots);
    }

    public void fetchAndUpsertAllTourSpots() {
        final int subListSize = 5000;
        List<TourApiTourInfo> tourInfoList = koreanInfoClient.getTourInfoList().block();

        for (int i = 0; i < tourInfoList.size(); i += subListSize) {
            List<TourApiTourInfo> subList = new ArrayList<>(tourInfoList.subList(i, Math.min(i + subListSize, tourInfoList.size())));
            upsertTourSpots(subList);
        }
    }


    public Mono<TourApiTourCommonDetail> fetchTourSpotDetail(String contentId) {
        return koreanInfoClient.getTourCommonDetail(contentId);
    }

    public Mono<List<TourApiTourImage>> fetchTourSpotImages(String contentId) {
        return koreanInfoClient.getTourImages(contentId);
    }
}
