package travelmate.backend.entity;

import lombok.Data;
import jakarta.persistence.*;
import travelmate.backend.repository.MemberRepository;

import java.util.Date;
@Entity
@Table(name = "preference")
@Data
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_id")
    private Integer preferenceId;

    @OneToOne
    @JoinColumn(name = "traveler_id", foreignKey = @ForeignKey(name = "preference_ibfk_1"))
    private Member traveler;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age_grp", nullable = false)
    private int ageGrp;

    @Column(name = "travel_start_ymd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date travelStartYmd;

    @Column(name = "travel_end_ymd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date travelEndYmd;

    @Column(name = "travel_styl_1", nullable = false)
    private int travelStyl1 = 4;  // 기본값 4으로 설정

    @Column(name = "travel_styl_2", nullable = false)
    private int travelStyl2 = 4;

    @Column(name = "travel_styl_3", nullable = false)
    private int travelStyl3 = 4;

    @Column(name = "travel_styl_4", nullable = false)
    private int travelStyl4 = 4;

    @Column(name = "travel_styl_5", nullable = false)
    private int travelStyl5 = 4;

    @Column(name = "travel_styl_6", nullable = false)
    private int travelStyl6 = 4;

    @Column(name = "travel_styl_7", nullable = false)
    private int travelStyl7 = 4;

    @Column(name = "travel_companions_num", nullable = false)
    private int travelCompanionsNum;
}