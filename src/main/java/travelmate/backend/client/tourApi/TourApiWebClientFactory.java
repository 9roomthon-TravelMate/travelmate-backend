package travelmate.backend.client.tourApi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


@Component
public class TourApiWebClientFactory {

    @Value("${spring.application.name}")
    private String appName;

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    public TourApiWebClientFactory(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper
                .copy()
                .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
    }

    public WebClient create(String baseUrl, String serviceKey) {
        UriComponentsBuilder uriComponentsBuilder = createUriComponentsBuilder(baseUrl, serviceKey);
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(uriComponentsBuilder);

        return webClientBuilder
                .clone()
                .uriBuilderFactory(uriBuilderFactory)
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().maxInMemorySize(50 * 1024 * 1024);
                    clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
                })
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
