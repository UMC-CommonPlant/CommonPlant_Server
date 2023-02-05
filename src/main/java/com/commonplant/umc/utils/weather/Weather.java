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

}