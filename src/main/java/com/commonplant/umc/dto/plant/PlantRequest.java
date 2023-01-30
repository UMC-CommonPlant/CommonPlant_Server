package com.commonplant.umc.dto.plant;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantRequest {

    @Data
    public static class addPlant {
        private String name;
        private String place;
        private LocalDate createdAt;
        private LocalDateTime wateredDate;
    }

    @Data
    public static class updatePlant {
        private String name;
        private String place;
        private LocalDateTime wateredDate;
    }
}
