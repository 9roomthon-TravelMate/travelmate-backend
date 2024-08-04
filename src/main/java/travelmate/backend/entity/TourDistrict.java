package travelmate.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "uniqueCodeInRegionIndex", columnList = "code, tour_region_id", unique = true))
public class TourDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_district_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_region_id")
    private TourRegion region;

    // TourAPI
    @NotNull
    private String code;

    @NotNull
    private String name;

    private String englishName;

    public TourDistrict(TourRegion region, String code, String name, String englishName) {
        this.region = region;
        this.code = code;
        this.name = name;
        this.englishName = englishName;
    }

    public void updateNames(String name, String englishName) {
        this.name = name;
        this.englishName = englishName;
    }

}
