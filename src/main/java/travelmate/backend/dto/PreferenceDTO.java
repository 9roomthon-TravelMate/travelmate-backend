package travelmate.backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PreferenceDTO {

    private String gender;
    private int ageGrp;
    private Date travelStartYmd;
    private Date travelEndYmd;
    private int travelStyl1;
    private int travelStyl2;
    private int travelStyl3;
    private int travelStyl4;
    private int travelStyl5;
    private int travelStyl6;
    private int travelStyl7;
    private int travelCompanionsNum;
    private int regionId;
}

