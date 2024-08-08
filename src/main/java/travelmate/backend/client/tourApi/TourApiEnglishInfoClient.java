package travelmate.backend.client.tourApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import travelmate.backend.dto.tourApi.TourApiResponse;
import travelmate.backend.dto.tourApi.item.TourApiAreaInfo;

import java.util.List;

@Component
public class TourApiEnglishInfoClient {

    private final WebClient webClient;

    private static final int PAGE_SIZE = 100_0000;

    public TourApiEnglishInfoClient(
            @Value("${tour-api.english-info.base-url}") String baseUrl,
            @Value("${tour-api.english-info.service-key}") String serviceKey,
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


}
