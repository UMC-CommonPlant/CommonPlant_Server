package com.commonplant.umc.dto.memo;

import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemoResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class memoCardRes{
        private Long memoIdx;

        // @JsonIgnore
        // private Plant plant;
        private Long plantIdx;

        // @JsonIgnore
        // private User user;
        private String userNickName;
        private String userImgUrl;


        private String content;
        private String imgUrl;

        @JsonFormat(pattern = "yyyy.MM.dd")
        private LocalDateTime createdAt;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class memoListRes{
        private List<List> memoCardDto = new ArrayList<>();
    }
}