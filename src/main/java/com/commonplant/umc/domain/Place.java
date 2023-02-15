
package com.commonplant.umc.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Table(name = "place")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Place extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_idx")
    private Long placeIdx;

    @Column(nullable = false)     // 장소 이름
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_idx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;      // 소유자 (리더)
    @Column(nullable = true)
    private String girdX;    // 경도 (x)
    @Column(nullable = true)
    private String gridY;     // 위도 (y)
    @Column(nullable = false)
    private String address;
    @Column(nullable = true)
    private String placeImgUrl;
    @Column(nullable = false)
    private String code;

    @Builder
    public Place(String name, User owner, String address, String placeImgUrl,String gridX, String gridY,  String code) {
        this.name = name;
        this.owner = owner;
        this.address = address;
        this.placeImgUrl = placeImgUrl;
        this.girdX = gridX;
        this.gridY = gridY;
        this.code = code;
    }

    public void setPlaceImgUrl(String placeImgUrl) {
        this.placeImgUrl = placeImgUrl;
    }

    public void setGirdX(String girdX) {
        this.girdX = girdX;
    }

    public void setGridY(String gridY) {
        this.gridY = gridY;
    }

}