package travelmate.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import travelmate.backend.dto.*;
import travelmate.backend.entity.Member;
import travelmate.backend.entity.Preference;
import travelmate.backend.repository.MemberRepository;
import travelmate.backend.repository.PreferenceRepository;
import travelmate.backend.repository.TourSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import travelmate.backend.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private TourSpotRepository tourSpotRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @Value("${fastapi.base-url}")
    private String fastApiUrl;

    @PostMapping("/submit")
    public ResponseEntity<List<TourSpotForRecommendDTO>> submitSurvey(@RequestBody PreferenceDTO preferenceDTO,
                                                                      @AuthenticationPrincipal CustomOAuth2User user) {

        // CustomOAuth2User에서 id를 가져옴
        Long userId = user.getId();

        Member traveler = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // traveler_id로 기존 Preference를 찾음
        Preference preference = preferenceRepository.findByTraveler(traveler)
                .orElse(new Preference()); // 없으면 새로운 Preference 객체 생성

        // Preference 설정
        preference.setTraveler(traveler);
        preference.setGender(preferenceDTO.getGender());
        preference.setAgeGrp(preferenceDTO.getAgeGrp());
        preference.setTravelStartYmd(preferenceDTO.getTravelStartYmd());
        preference.setTravelEndYmd(preferenceDTO.getTravelEndYmd());
        preference.setTravelStyl1(preferenceDTO.getTravelStyl1());
        preference.setTravelStyl2(preferenceDTO.getTravelStyl2());
        preference.setTravelStyl3(preferenceDTO.getTravelStyl3());
        preference.setTravelStyl4(preferenceDTO.getTravelStyl4());
        preference.setTravelStyl5(preferenceDTO.getTravelStyl5());
        preference.setTravelStyl6(preferenceDTO.getTravelStyl6());
        preference.setTravelStyl7(preferenceDTO.getTravelStyl7());
        preference.setTravelCompanionsNum(preferenceDTO.getTravelCompanionsNum());

        preferenceRepository.save(preference);

        // FastAPI 서버에 추천 요청
        String fullUrl = fastApiUrl + "/recommend/"+ userId + "?region_id=" + preferenceDTO.getRegionId();
        ResponseEntity<RecommendationResponse> response = restTemplate.getForEntity(fullUrl, RecommendationResponse.class);

        // 추천된 content_id 리스트에서 관광지 정보를 조회
        List<String> contentIds = response.getBody().getRecommendations();
        List<TourSpotForRecommendDTO> tourSpots = tourSpotRepository.findByContentIdIn(contentIds);

        return ResponseEntity.ok(tourSpots);
    }

    @PostMapping("/save-visited")
    public ResponseEntity<Void> saveVisitedPlaces(@RequestBody SaveVisitedRequest request,
                                                  @AuthenticationPrincipal CustomOAuth2User user) {

        Long userId = user.getId();

        recommendationService.saveVisitedPlaces(userId, request.getContentIds());
        return ResponseEntity.ok().build();
    }
}
