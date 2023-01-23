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
    private Long placeIdx;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "status")
    private Long status;

    @Column(name = "AccessToken")
    private Long AccessToken;

    @Column(name = "platform")
    private Long platform;

    @Column(nullable = true)
    private String userImgUrl;

    @Builder
    public void User(String name, String email, String password, Long status, Long AccessToken, Long platform, String userImgUrl){
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
        this.AccessToken = AccessToken;
        this.platform = platform;
        this.userImgUrl = userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl){
        this.userImgUrl = userImgUrl;
    }
}