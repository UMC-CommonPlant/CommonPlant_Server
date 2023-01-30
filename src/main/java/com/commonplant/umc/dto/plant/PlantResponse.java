package com.commonplant.umc.dto.plant;

import com.commonplant.umc.domain.Place;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PlantResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class plantCardRes{
        private Long plantIdx;

        private String name;
        private Place place;
        private String imgUrl;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime wateredDate;
    }
}
