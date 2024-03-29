package com.commonplant.umc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Table(name = "memo")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Memo extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_idx")
    private Long memoIdx;

    @ManyToOne
    @JoinColumn(name = "plant_idx", nullable = false)
    private Plant plant;

    // CASCADE: 메모는 1명만 작성 가능하니까 팀이랑 상관없을 듯
    @ManyToOne
    @JoinColumn(name = "user_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User writer;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = true, name = "img_url")
    private String imgUrl;

    @Builder
    public Memo(Plant plant, User writer, String content, String imgUrl){
        this.plant = plant;
        this.writer = writer;
        this.content = content;
        this.imgUrl = imgUrl;
    }

    // Memo 테이블의 plant 칼럼은 plant_idx!!!
    public void updateMemo(String content, String imgUrl){
        // this.plant = plant;
        // this.writer = writer;
        this.content = content;
        this.imgUrl = imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
