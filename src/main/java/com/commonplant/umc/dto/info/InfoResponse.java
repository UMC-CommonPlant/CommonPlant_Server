package com.commonplant.umc.dto.info;

import com.commonplant.umc.domain.Info;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class InfoResponse {
    @NoArgsConstructor
    @Data
    public static class getSearchList{
        private String name;
        private String imgUrl;
        private String scientific_name;

        @Builder
        public getSearchList(Info info){
            this.name = info.getName();
            this.imgUrl = info.getImgUrl();
            this.scientific_name = info.getScientific_name();
        }
    }
}
