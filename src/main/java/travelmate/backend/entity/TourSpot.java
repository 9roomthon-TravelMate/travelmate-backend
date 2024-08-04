package travelmate.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import travelmate.backend.dto.tourApi.item.TourApiTourInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "uniqueContentIdIndex", columnList = "content_id", unique = true),
        @Index(name = "nameIndex", columnList = "name"),
        @Index(name = "tourSpotThemeIndex", columnList = "tour_spot_theme_id"),
        @Index(name = "regionIndex", columnList = "region_id"),
        @Index(name = "districtIndex", columnList = "district_id")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private TourRegion region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
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

    private LocalDateTime imagesUpdatedAt;


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
        this.mainImageUrl = tourApiTourInfo.firstimage();
        this.mainThumbnailUrl = tourApiTourInfo.firstimage2();
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
        this.imagesUpdatedAt = LocalDateTime.now();
    }

}
