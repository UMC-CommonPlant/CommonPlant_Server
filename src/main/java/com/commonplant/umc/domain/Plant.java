package com.commonplant.umc.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(nullable = false, length = 10)
    private String name;

    // 장소 입력: 필수 (Garden 6-2)
    @Column(nullable = false)
    private String place;

    @Column(nullable = true, name = "img_url")
    private String imgUrl;

    private LocalDateTime wateredDate;

    @OneToMany(mappedBy = "plant")
    private List<Memo> memoList = new ArrayList<>();

    @Builder
    public Plant(String name, String place, String imgUrl, LocalDateTime wateredDate) {
        this.name = name;
        this.place = place;
        this.imgUrl = imgUrl;
        this.wateredDate = wateredDate;
    }

    public void updatePlant(String name, String place, String imgUrl){
        this.name = name;
        this.place = place;
        this.imgUrl = imgUrl; // 아직 ...
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
