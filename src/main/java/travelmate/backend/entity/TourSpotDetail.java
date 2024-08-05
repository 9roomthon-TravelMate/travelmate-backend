package travelmate.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import travelmate.backend.dto.tourApi.item.TourApiTourCommonDetail;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TourSpotDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_spot_detail_id")
    private Long id;

    @Column(length = 1000)
    private String overview;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public TourSpotDetail(TourApiTourCommonDetail detail) {
        this.overview = detail.overview();
    }
}
