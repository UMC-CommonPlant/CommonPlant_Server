
package com.commonplant.umc.dto.place;

import lombok.*;

import java.util.List;

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
    public static class updatePlace {
        private String name;
        private String address;
        private String placeCode;
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
    public static class addPeople{
        private String name;
        private String placeCode;

    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class deletePeople{
        private List<String> name;
        private String placeCode;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class address{
        private String address;
    }
}
