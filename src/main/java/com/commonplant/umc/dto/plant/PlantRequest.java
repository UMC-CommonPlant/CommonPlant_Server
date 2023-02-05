package com.commonplant.umc.dto.plant;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantRequest {

    @Data
    public static class addPlant {

        private String name;
        private String nickname;
        private String place;
        private LocalDate createdAt;
        private LocalDateTime wateredDate;
    }

    @Data
    public static class updatePlant {
        private String nickname;
        private String place;
        private LocalDateTime wateredDate;
    }

    @Data
    public static class updateWateredDate {
        private LocalDateTime wateredDate;
    }
}
