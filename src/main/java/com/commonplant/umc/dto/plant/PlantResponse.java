package com.commonplant.umc.dto.plant;

import com.commonplant.umc.domain.Place;
import com.commonplant.umc.dto.memo.MemoResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlantResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class plantCardRes{
        private Long plantIdx;

        private String name;
        private String nickname;
        // @JsonIgnore
        private Place place;
        private String imgUrl;

        private Long countDate;
        private Long remainderDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime wateredDate;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class plantAndMemoRes{
        private plantCardRes plant;
        private MemoResponse.memoListRes memoList;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class plantListRes{
        private List<List> plantCardDto = new ArrayList<>();
    }

}
