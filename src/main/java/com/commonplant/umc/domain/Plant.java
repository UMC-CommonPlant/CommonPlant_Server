package com.commonplant.umc.domain;

import lombok.*;

import javax.persistence.*;

//@Data -> @Getter + @Setter
@Getter
@Table(name = "plant")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_idx")
    private Long plantIdx;

    // 식물 애칭
    @Column(length = 10, nullable = false)
    private String name;

    // 장소 입력: 필수 (Garden 6-2)
    @Column(nullable = false)
    private String place;

    @Column(nullable = true, name = "img_url")
    private String imgUrl;

    @Builder
    public Plant(String name, String place, String imgUrl) {
        this.name = name;
        this.place = place;
        this.imgUrl = imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
