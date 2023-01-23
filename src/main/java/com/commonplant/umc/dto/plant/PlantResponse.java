package com.commonplant.umc.dto.plant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PlantResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class plantCardRes{
        private Long plantIdx;

        private String name;
        private String place;
        private String imgUrl;
    }
}
