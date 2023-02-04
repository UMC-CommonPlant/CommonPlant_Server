package com.commonplant.umc.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequest {
    private String name;
    private String nickName;
    private String email;

    public UserRequest( String name, String nickName, String email) {
        this.name = name;
        this.nickName = nickName;
        this.email = email;
    }
}

