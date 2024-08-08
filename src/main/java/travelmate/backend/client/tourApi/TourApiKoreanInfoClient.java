package travelmate.backend.client.tourApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import travelmate.backend.dto.tourApi.TourApiResponse;
import travelmate.backend.dto.tourApi.item.TourApiAreaInfo;
import travelmate.backend.dto.tourApi.item.TourApiTourCommonDetail;
import travelmate.backend.dto.tourApi.item.TourApiTourImage;
import travelmate.backend.dto.tourApi.item.TourApiTourInfo;

import java.util.List;

@Component
public class TourApiKoreanInfoClient {

    private final WebClient webClient;

    private static final int PAGE_SIZE = 100_0000;

    public TourApiKoreanInfoClient(
            @Value("${tour-api.korean-info.base-url}") String baseUrl,
            @Value("${tour-api.korean-info.service-key}") String serviceKey,
            TourApiWebClientFactory webClientFactory) {
        this.webClient = webClientFactory.create(baseUrl, serviceKey);
    }

    public Mono<List<TourApiAreaInfo>> getRegionInfoList() {
        return getAreaInfoList(null);
    }

    public Mono<List<TourApiAreaInfo>> getDistrictInfoList(String regionCode) {
        return getAreaInfoList(regionCode);
    }

    private Mono<List<TourApiAreaInfo>> getAreaInfoList(String areaCode) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/areaCode1")
                            .queryParam("numOfRows", PAGE_SIZE);
                    if (areaCode != null) {
                        uriBuilder.queryParam("areaCode", areaCode);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<TourApiAreaInfo>>() {})
                .map(data -> data.body().items());
    }

    public Mono<List<TourApiTourInfo>> getTourInfoList() {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/areaBasedList1")
                            .queryParam("numOfRows", PAGE_SIZE);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<TourApiTourInfo>>() {})
                .map(data -> data.body().items());
    }

    public Mono<TourApiTourCommonDetail> getTourCommonDetail(String contentId) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/detailCommon1")
                            .queryParam("numOfRows", PAGE_SIZE)
                            .queryParam("defaultYN", "Y")
                            .queryParam("firstImageYN", "Y")
                            .queryParam("areacodeYN", "Y")
                            .queryParam("catcodeYN", "Y")
                            .queryParam("addrinfoYN", "Y")
                            .queryParam("mapinfoYN", "Y")
                            .queryParam("overviewYN", "Y")
                            .queryParam("contentId", contentId);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<TourApiTourCommonDetail>>() {})
                .map(data -> data.body().items().get(0))
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<List<TourApiTourImage>> getTourImages(String contentId) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/detailImage1")
                            .queryParam("numOfRows", PAGE_SIZE)
                            .queryParam("imageYN", "Y")
                            .queryParam("subImageYN", "Y")
                            .queryParam("contentId", contentId);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<TourApiTourImage>>() {})
                .map(data -> data.body().items())
                .onErrorResume(e -> Mono.empty());
    }

}
