package travelmate.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import travelmate.backend.dto.tourApi.item.TourApiTourInfo;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "uniqueContentIdIndex", columnList = "content_id", unique = true),
        @Index(name = "nameIndex", columnList = "name"),
        @Index(name = "tourSpotThemeIndex", columnList = "tour_spot_theme_id"),
        @Index(name = "regionIndex", columnList = "tour_region_id"),
        @Index(name = "districtIndex", columnList = "tour_district_id"),
        @Index(name = "mainImageUrlIndex", columnList = "main_image_url"),
        @Index(name = "mainThumbnailUrlIndex", columnList = "main_thumbnail_url"),
})
public class TourSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_spot_id")
    private Long id;

    // TourAPI
    @NotNull
    private String contentId;

    // TourAPI
    private String contentTypeId;

    private String name;

    private String address;

    @Column(precision = 12, scale = 10)
    private BigDecimal latitude;

    @Column(precision = 13, scale = 10)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_region_id")
    private TourRegion region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_district_id")
    private TourDistrict district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_spot_theme_id")
    private TourSpotThemeDetail themeDetail;

    private String mainImageUrl;
    private String mainThumbnailUrl;

    @OneToOne
    @JoinColumn(name = "tour_spot_detail_id")
    private TourSpotDetail detail;

    @OneToMany(mappedBy = "tourSpot")
    private List<TourSpotImage> images = new ArrayList<>();

    private Instant imagesUpdatedAt;

    // TourAPI
    private String category1;
    private String category2;
    private String category3;

    public TourSpot(TourApiTourInfo tourApiTourInfo, TourRegion region, TourDistrict district, TourSpotThemeDetail themeDetail) {
        update(tourApiTourInfo, region, district, themeDetail);
    }

    public void update(TourApiTourInfo tourApiTourInfo, TourRegion region, TourDistrict district, TourSpotThemeDetail themeDetail) {
        this.contentId = tourApiTourInfo.contentid();
        this.contentTypeId = tourApiTourInfo.contenttypeid();
        this.name = tourApiTourInfo.title();
        this.address = tourApiTourInfo.addr1();
        this.latitude = new BigDecimal(tourApiTourInfo.mapy());
        this.longitude = new BigDecimal(tourApiTourInfo.mapx());
        this.mainImageUrl = StringUtils.hasText(tourApiTourInfo.firstimage()) ? tourApiTourInfo.firstimage() : null;
        this.mainThumbnailUrl = StringUtils.hasText(tourApiTourInfo.firstimage2()) ? tourApiTourInfo.firstimage2() : null;
        this.category1 = tourApiTourInfo.cat1();
        this.category2 = tourApiTourInfo.cat2();
        this.category3 = tourApiTourInfo.cat3();
        this.region = region;
        this.district = district;
        this.themeDetail = themeDetail;
    }

    public void updateDetail(TourSpotDetail detail) {
        this.detail = detail;
    }

    public void updateImagesUpdatedAt() {
        this.imagesUpdatedAt = Instant.now();
    }

}
