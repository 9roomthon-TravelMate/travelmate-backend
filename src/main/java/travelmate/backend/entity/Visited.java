package travelmate.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "visited")
public class Visited {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitId;

    @ManyToOne
    @JoinColumn(name = "traveler_id", nullable = false)
    private Member traveler;

    @Column(name = "content_id", nullable = false)
    private String contentId;

    // Getters and Setters
    public Long getVisitId() {
        return visitId;
    }

    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }

    public Member getTraveler() {
        return traveler;
    }

    public void setTraveler(Member traveler) {
        this.traveler = traveler;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}