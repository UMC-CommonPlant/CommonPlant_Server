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
        
        // 식물 수정 화면에서 장소 수정은 이루어지지 않음 
        // private String place;
        
        // updateWateredDate로 이동
        // private LocalDateTime wateredDate;
    }

    @Data
    public static class updateWateredDate {
        private LocalDateTime wateredDate;
    }
}
