package travelmate.backend.client.tourApi;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class TourApiWebClientFactory {

    private final WebClient.Builder webClientBuilder;

    @Value("${spring.application.name}")
    private String appName;

    public WebClient create(String baseUrl, String serviceKey) {
        UriComponentsBuilder uriComponentsBuilder = createUriComponentsBuilder(baseUrl, serviceKey);
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(uriComponentsBuilder);

        return webClientBuilder
                .clone()
                .uriBuilderFactory(uriBuilderFactory)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(50*1024*1024))
                .build();
    }

    private UriComponentsBuilder createUriComponentsBuilder(String baseUrl, String serviceKey) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", appName)
                .queryParam("serviceKey", "{serviceKey}")
                .queryParam("_type", "json")
                .uriVariables(Map.of(
                        "serviceKey", serviceKey
                ));
    }

}
