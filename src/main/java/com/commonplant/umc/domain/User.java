package com.commonplant.umc.domain;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UUID")
    private UUID UUID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickName;

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
    public void User(UUID UUID, String name, String nickName, String email, String password, Long status, Long AccessToken, Long platform, String userImgUrl){
        this.UUID = UUID;
        this.name = name;
        this.nickName = nickName;
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