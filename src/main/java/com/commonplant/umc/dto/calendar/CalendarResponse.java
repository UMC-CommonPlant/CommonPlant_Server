package com.commonplant.umc.dto.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarResponse {

    // plantCreatedAtRes, plantWateredDateRes는 나중에 디자인 바뀌면 변경 가능
    // 화면에 출력되어야 하는 정보'만' 작성해야 함
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class plantCreatedAtRes{
        private Long plantIdx;

        private String name;
        private String nickname;
        private String place;
        private String imgUrl;

        @JsonFormat(pattern = "HH:mm")
        private LocalDateTime createdAt;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class plantWateredDateRes{
        private Long plantIdx;

        private String name;
        private String nickname;
        private String place;
        private String imgUrl;

        @JsonFormat(pattern = "HH:mm")
        private LocalDateTime wateredDate;
    }


    // memo쪽 response를 구현하려면 memoRepository에 아랫줄 추가
    // List<Memo> findAllByUser(User user);

    @NoArgsConstructor
    @Data
    public static class calendarCardListRes{

        // List<List> -> List<plantCreatedAtRes>, List<plantWateredDateRes>
        private List<plantCreatedAtRes> plantCreatedAtDto = new ArrayList<>();

        private List<plantWateredDateRes> plantWateredDateDto = new ArrayList<>();

        @Builder
        public calendarCardListRes(List<plantCreatedAtRes> plantCreatedAtDto, List<plantWateredDateRes> plantWateredDateDto){
            this.plantCreatedAtDto = plantCreatedAtDto;
            this.plantWateredDateDto = plantWateredDateDto;
        }
    }
}
