package travelmate.backend.dto.tourApi.item;

public record TourApiTourImage(
        String contentid,
        String imgname,
        String originimgurl,
        String serialnum,
        String smallimageurl
) {}
