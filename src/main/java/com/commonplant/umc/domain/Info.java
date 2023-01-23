package com.commonplant.umc.domain;

import com.google.cloud.Timestamp;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Info {

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    private String name;
    private String humidity;
    private String management;
    private String place;
    private String scientific_name;
    private Long water_day;
    private String sunlight;
    private Long temp_max;
    private Long temp_min;
    private String tip;
    private String water_spring;
    private String water_autumn;
    private String water_winter;
    private String water_summer;
    private Timestamp created_at;
}
