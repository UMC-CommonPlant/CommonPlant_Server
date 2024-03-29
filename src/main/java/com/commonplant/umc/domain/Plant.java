package com.commonplant.umc.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @ManyToOne
    @JoinColumn(name = "place_idx", nullable = false)
    private Place place;

    @Column(nullable = true, name = "img_url")
    private String imgUrl;

    @Column(nullable = false, name = "watered_date")
    private LocalDateTime wateredDate;

    @Column(nullable = true)
    private Long remainderDate;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public Plant(String name, User user, String nickname, Place place, String imgUrl, LocalDateTime wateredDate) {
        this.name = name;
        this.user = user;
        this.nickname = nickname;
        this.place = place;
        this.imgUrl = imgUrl;
        this.wateredDate = wateredDate;
    }

    public void updatePlant(String nickname, String imgUrl){
        this.nickname = nickname;
        // this.place = place;
        this.imgUrl = imgUrl;
        // this.wateredDate = wateredDate;
    }

    // updatePlant()/setWateredDate(): wateredDate 수정은 어디서?
    public void setWateredDate(LocalDateTime wateredDate) {

        // DateTimeFormatter
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String parsedUpdatedWateredDate = LocalDate.now().toString();
        System.out.println("========parsedUpdatedWateredDate from Plant Domain======== " + parsedUpdatedWateredDate);

        LocalDate updatedWateredDate = LocalDate.parse(parsedUpdatedWateredDate, dateTimeFormatter);
        LocalDateTime updatedWateredDateTime = updatedWateredDate.atStartOfDay();

        System.out.println("========updatedWateredDateTime from Plant Domain======== " + updatedWateredDate);
        this.wateredDate = updatedWateredDateTime;
        // this.wateredDate = wateredDate;

    }

    public void setRemainderDate(Long remainderDate)
    {
        this.remainderDate = remainderDate;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
