package com.commonplant.umc.dto.place;

import lombok.*;

public class PlaceRequest {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class addPlace {
        private String name;
        private String address;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class updatePlace {
        private String name;
        private String address;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class searchPeople{
        private String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class adress{
        private String adress;
    }
}
