package com.commonplant.umc.domain;

import lombok.*;

import javax.persistence.*;


@Getter
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long userIdx;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String email;


    @Column(name = "status", columnDefinition = "varchar(20) default 'ACTIVE'")
    private String status;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "platform")
    private String platform;

    @Column(nullable = true)
    private String userImgUrl;


    @Builder
    public User(String uuid, String nickName, String email, String platform, String userImgUrl){
        this.uuid = uuid;
        this.nickName = nickName;
        this.email = email;
        this.platform = platform;
        this.userImgUrl = userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl){
        this.userImgUrl = userImgUrl;
    }
    public void update(String nickName, String email){
        this.nickName = nickName;
        this.email = email;
    }
}