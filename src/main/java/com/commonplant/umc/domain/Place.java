package com.commonplant.umc.domain;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Place extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_idx")
    private Long placeIdx;

    @Column(nullable = false)
    private String name;

 //   @Column(nullable = false)
 //   private double latitude;

 //    @Column(nullable = false)
 //   private double longitude;
    @Column(nullable = false)
    private String adress;
    @Column(nullable = true)
    private Long plant;
    @Column(nullable = false)
    private String placeImgUrl;

    @Builder
    public Place(String name, String adress, String placeImgUrl) {
        this.name = name;
        this.adress = adress;
        this.placeImgUrl = placeImgUrl;
    }

    public void setPlaceImgUrl(String placeImgUrl) {
        this.placeImgUrl = placeImgUrl;
    }


}
