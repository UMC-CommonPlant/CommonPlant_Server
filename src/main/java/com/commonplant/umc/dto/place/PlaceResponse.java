package com.commonplant.umc.dto.place;

import com.commonplant.umc.domain.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class PlaceResponse {

    @NoArgsConstructor
    @Data
    public static class getPlaceInfo{

        private String name;
        private String address;

        private String highestTemp;    // 최고기온

        private String minimumTemp;   // 최저기온

        private String humidity;     // 습도

        @Builder
        public getPlaceInfo(Place place){
            this.name = place.getName();
            this.address = place.getAddress();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class searchPeople{
        private List<String> name;
        private String ImgUrl;
    }

}
