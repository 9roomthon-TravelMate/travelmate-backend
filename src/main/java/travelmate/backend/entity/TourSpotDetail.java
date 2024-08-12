package travelmate.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import travelmate.backend.dto.tourApi.item.TourApiTourCommonDetail;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
public class TourSpotDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_spot_detail_id")
    private Long id;

    @Column(length = 3000)
    private String overview;

    @UpdateTimestamp
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.updatedAt = Instant.now();
    }

    public TourSpotDetail(TourApiTourCommonDetail detail) {
        this.overview = detail.overview();
    }
}
