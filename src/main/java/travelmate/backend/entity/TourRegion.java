package travelmate.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "uniqueCodeIndex", columnList = "code", unique = true))
public class TourRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_region_id")
    private Long id;

    @OneToMany(mappedBy = "region")
    private List<TourDistrict> districts = new ArrayList<>();

    // TourAPI
    @NotNull
    private String code;

    @NotNull
    private String name;

    private String englishName;

    private String imageUrl;

    public TourRegion(String code, String name, String englishName) {
        this.code = code;
        this.name = name;
        this.englishName = englishName;
    }

    public void updateNames(String name, String englishName) {
        this.name = name;
        this.englishName = englishName;
    }

}
