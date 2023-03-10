package com.commonplant.umc.dto.plant;

import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.Plant;
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
        // private Place place;
        private String place;

        private String imgUrl;

        private Long countDate;
        private Long remainderDate;

        // Info
        private String scientificName;
        private Long waterDay;
        private String sunlight;
        private Long tempMin;
        private Long tempMax;
        private String humidity;

        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy.MM.dd")
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class plantOfPlaceRes{

        private Long plantIdx;    // 식물 PK
        private String name;       // 식물 종 이름 (학명)
        private String nickname;   // 애칭
        private String imgUrl;     // 식물 이미지
        private String recentMemo;   // 제일 최근 메모
        private Long remainderDate;   // 물주기 D-Day
        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime wateredDate;   // 마지막으로 물 준 날

        @Builder
        public plantOfPlaceRes(Plant plant, String recentMemo)
        {
            this.name = plant.getName();
            this.nickname = plant.getNickname();
            this.plantIdx = plant.getPlantIdx();
            this.imgUrl = plant.getImgUrl();
            this.remainderDate = plant.getRemainderDate();
            this.wateredDate = plant.getWateredDate();
            this.recentMemo = recentMemo;
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class plantUpdateCardRes{
        private Long plantIdx;

        private String nickName;
        private String imgUrl;
    }

}
