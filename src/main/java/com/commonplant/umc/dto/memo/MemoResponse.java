package com.commonplant.umc.dto.memo;

import com.commonplant.umc.domain.Plant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    }
}
