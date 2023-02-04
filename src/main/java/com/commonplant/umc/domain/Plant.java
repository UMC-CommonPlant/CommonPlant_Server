package com.commonplant.umc.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    
    // 식물 이름
    @Column(nullable = false)
    private String name;
    
    // 식물 애칭
    // Postman에서 메모 등록할 때 10자 이상 넣으면 500 발생!!!
    // 잘 매핑된 것으로 보임!!!
    @Column(nullable = false, length = 10)
    private String nickname;

    // 장소 입력: 필수 (Garden 6-2)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_idx", nullable = false)
    private Place place;

    @Column(nullable = true, name = "img_url")
    private String imgUrl;

    @Column(nullable = false, name = "watered_date")
    private LocalDateTime wateredDate;

    // LocalDate: 2023-01-24
    // LocalDateTime: 2023-01-24T...
    // PlantRequest와 PlantResponse의 내용을 수정해서 구현
//    @Column(nullable = false, name = "created_at", updatable = false)
//    private LocalDate createdAt;

    @Builder
    public Plant(String name, String nickname, Place place, String imgUrl, LocalDateTime wateredDate) {
        this.name = name;
        this.nickname = nickname;
        this.place = place;
        this.imgUrl = imgUrl;
        this.wateredDate = wateredDate;
    }

    public void updatePlant(String nickname, String imgUrl, LocalDateTime wateredDate){
        this.nickname = nickname;
        // this.place = place;
        this.imgUrl = imgUrl;
        this.wateredDate = wateredDate;
    }

    // updatePlant()/setWateredDate(): wateredDate 수정은 어디서?
    public void setWateredDate(LocalDateTime wateredDate) {

        // DateTimeFormatter
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String parsedUpdatedWateredDate = LocalDate.now().toString();
        System.out.println("========parsedUpdatedWateredDate from Plant Domain======== " + parsedUpdatedWateredDate);

        LocalDate updatedWateredDate = LocalDate.parse(parsedUpdatedWateredDate, dateTimeFormatter);
        LocalDateTime updatedWateredDateTime = updatedWateredDate.atStartOfDay();

        this.wateredDate = updatedWateredDateTime;
        // this.wateredDate = wateredDate;

    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
