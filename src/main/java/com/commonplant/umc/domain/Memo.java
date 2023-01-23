package com.commonplant.umc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "plant_idx", nullable = false)
    private Plant plant;

    @Column(nullable = false)
    private String user;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(nullable = true, name = "img_url")
    private String imgUrl;

    @Builder
    public Memo(Plant plant, String user, String content){
        this.plant = plant;
        this.user = user;
        this.content = content;
    }

    // Memo 테이블의 plant 칼럼은 plant_idx!!!
    public void updateMemo(Plant plant, String user, String content, String imgUrl){
        this.plant = plant;
        this.user = user;
        this.content = content;
        this.imgUrl = imgUrl; // 아직...
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
