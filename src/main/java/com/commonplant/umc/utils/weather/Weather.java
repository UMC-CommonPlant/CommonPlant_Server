package com.commonplant.umc.utils.weather;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class Weather{

@NoArgsConstructor
@AllArgsConstructor
@Data
public static class LatXLngY
{
    public double lat;
    public double lng;

    public double x;
    public double y;

}

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class weatherInfo
    {
        private String highestTemp;    // 최고기온

        private String minimumTemp;   // 최저기온

        private String humidity;     // 습도

    }



}