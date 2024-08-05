package travelmate.backend.client.tourApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TourApiPhotoGalleryClient {

    private final WebClient webClient;

    public TourApiPhotoGalleryClient(
            @Value("${tour-api.photo-gallery.base-url}") String baseUrl,
            @Value("${tour-api.photo-gallery.service-key}") String serviceKey,
            TourApiWebClientFactory webClientFactory) {
        this.webClient = webClientFactory.create(baseUrl, serviceKey);
    }


}
