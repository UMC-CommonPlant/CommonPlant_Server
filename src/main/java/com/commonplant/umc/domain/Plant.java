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
public class Plant extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_idx")
    private Long plantIdx;

    // 식물 애칭
    // Postman에서 메모 등록할 때 10자 이상 넣으면 500 발생!!!
    // 잘 매핑된 것으로 보임!!!
    @Column(nullable = false, length = 10)
    private String name;

    // 장소 입력: 필수 (Garden 6-2)
    @Column(nullable = false)
    private String place;

    @Column(nullable = true, name = "img_url")
    private String imgUrl;

//    @Column(nullable = false, name = "watered_date")
//    private LocalDateTime wateredDate;

//    @Column(nullable = false, name = "created_at")
//    private LocalDateTime createdAt;

//    @OneToMany(mappedBy = "plant")
//    private List<Memo> memoList = new ArrayList<>();

    @Builder
    public Plant(String name, String place, String imgUrl) {
        this.name = name;
        this.place = place;
        this.imgUrl = imgUrl;
        // this.wateredDate = wateredDate;
    }

    public void updatePlant(String name, String place, String imgUrl){
        this.name = name;
        this.place = place;
        this.imgUrl = imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
