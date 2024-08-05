package travelmate.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import travelmate.backend.dto.tourApi.item.TourApiTourImage;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "uniqueSerialNumIndex", columnList = "serialNum", unique = true))
public class TourSpotImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_spot_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_spot_id")
    private TourSpot tourSpot;

    private String name;

    private String url;

    private String thumbnailUrl;

    // tourAPI
    private String serialNum;

    public TourSpotImage(TourApiTourImage image, TourSpot tourSpot) {
        this.tourSpot = tourSpot;
        this.name = image.imgname();
        this.url = image.originimgurl();
        this.thumbnailUrl = image.smallimageurl();
        this.serialNum = image.serialnum();
    }

}
