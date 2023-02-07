package com.commonplant.umc.dto.place;

import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.plant.PlantResponse;
import com.commonplant.umc.utils.weather.Weather;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class PlaceResponse {

    @NoArgsConstructor
    @Data
    public static class getPlaceInfo{
        private String name;     // 장소 명
        private String address;    // 주소

        // -------------- 날씨 정보 ----------------------
        private String highestTemp;    // 최고기온
        private String minimumTemp;   // 최저기온
        private String humidity;     // 습도

        // ------------- 참여 인원 정보 -------------------

        private Boolean isOwner;    // 내가 리더인지 아닌지

        private List<userInfoList> userInfoList;


        // --------------- 식물 정보 ----------------------

        private List<PlantResponse.plantOfPlaceRes> plantInfoList;

        @Builder
        public getPlaceInfo(Place place,  Boolean isOwner, List<userInfoList> userInfoList,
                            List<PlantResponse.plantOfPlaceRes> plantInfoList){
            this.name = place.getName();
            this.address = place.getAddress();
            this.highestTemp = "8";
            this.minimumTemp = "-4";
            this.humidity = "77%";
            this.isOwner = isOwner;
            this.userInfoList = userInfoList;
            this.plantInfoList = plantInfoList;

        }
    }

    @NoArgsConstructor
    @Data
    public static class getMainPage{
        public String nickName; // (현재) 사용자 닉네임
        public List<placeList> placeList;   // 장소 목록
        public List<plantList> plantList;   // 식물 목록

        @Builder
        public getMainPage(String nickName, List<placeList> placeList, List<plantList> plantList)
        {
            this.nickName = nickName;
            this.placeList = placeList;
            this.plantList = plantList;
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class placeList{
        private String placeCode;   // 장소 PK

        private String placeName;   // 장소 이름

        private String ImgUrl;      // 장소 이미지

        private Long countUser;      // 참여 인원수

        private Long countPlant;     // 등록된 식물 수

        @Builder
        public placeList(Place place, Long countUser, Long countPlant)
        {
            this.placeCode = place.getCode();
            this.placeName = place.getName();
            this.ImgUrl = place.getPlaceImgUrl();

            this.countUser = countUser;
            this.countPlant = countPlant;
        }

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class plantList{
        private Long plantIdx;       // 식물 PK

        private String plantName;    // 식물 이름

        private String ImgUrl;       // 식물 이미지

        private String placeCode;     // 속한 장소

        private Long countUserOfPlace;  // 속한 장소 사람 수

        private Long remainderDate;    // 물주기 D-day

        @Builder
        public plantList(Plant plant, String placeCode, Long countUser)
        {
            this.plantIdx = plant.getPlantIdx();
            this.plantName = plant.getName();
            this.ImgUrl = plant.getImgUrl();
            this.placeCode = placeCode;
            this.countUserOfPlace = countUser;
            this.remainderDate = plant.getRemainderDate();
        }

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class searchPeople{
        private String nickName;

        private String ImgUrl;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class userInfoList{
        private String nickName;
        private String ImgUrl;
        private boolean isOwner;   // 이 유저가 리더인지 아닌지

        @Builder
        public userInfoList (User user, boolean isOwner)
        {
            this.nickName = user.getNickName();
            this.ImgUrl = user.getUserImgUrl();
            this.isOwner = isOwner;
        }
    }

}
