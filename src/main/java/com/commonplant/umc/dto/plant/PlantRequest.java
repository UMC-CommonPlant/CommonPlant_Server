package com.commonplant.umc.dto.plant;

import lombok.Data;

public class PlantRequest {

    @Data
    public static class addPlant {
        private String name;
        private String place;
    }

    @Data
    public static class updatePlant {
        private String name;

        private String place;

        private String imgUrl;
    }
}
