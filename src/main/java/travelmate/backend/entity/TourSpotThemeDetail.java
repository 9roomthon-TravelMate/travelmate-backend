package travelmate.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="tour_spot_theme",
        indexes = @Index(name = "uniqueCodeIndex", columnList = "code", unique = true))
public class TourSpotThemeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_spot_theme_id")
    private Long id;

    @NotNull
    @Column(name = "code")
    private TourSpotTheme theme;

    private String title;

    public TourSpotThemeDetail(TourSpotTheme theme, String title) {
        this.theme = theme;
        this.title = title;
    }
}
