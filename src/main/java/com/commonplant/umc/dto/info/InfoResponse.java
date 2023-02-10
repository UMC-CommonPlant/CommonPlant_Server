package com.commonplant.umc.dto.info;

import com.commonplant.umc.domain.Info;
import com.google.cloud.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class InfoResponse {
    @NoArgsConstructor
    @Data
    public static class getSearchList{
        private String name;
        private String imgUrl;
        private String scientific_name;

        @Builder
        public getSearchList(Info info){
            this.name = info.getName();
            this.imgUrl = info.getImgUrl();
            this.scientific_name = info.getScientific_name();
        }
    }

    @NoArgsConstructor
    @Data
    public static class getPlantInfo{

        public void setMonthValue(int month) {
            this.month = month;
        }
        public void setWaterType(String water_type) {
            this.water_type = water_type;
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
        private String water_type;
        private int month;
        private String imgUrl;
        private Timestamp created_at;

        @Builder
        public getPlantInfo(Info info){
            this.name = info.getName();
            this.humidity = info.getHumidity();
            this.management = info.getManagement();
            this.place = info.getPlace();
            this.scientific_name = info.getScientific_name();
            this.water_day = info.getWater_day();
            this.sunlight = info.getSunlight();
            this.temp_max = info.getTemp_max();
            this.temp_min = info.getTemp_min();
            this.tip = info.getTip();
            this.imgUrl = info.getImgUrl();
            this.created_at = info.getCreated_at();
        }
    }
}
