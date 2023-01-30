package com.commonplant.umc.dto.memo;

import com.commonplant.umc.domain.Plant;
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

        @JsonIgnore
        private Plant plant;
        private String user;
        private String content;
        private String imgUrl;

        @JsonFormat(pattern = "yyyy-MM-dd")
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