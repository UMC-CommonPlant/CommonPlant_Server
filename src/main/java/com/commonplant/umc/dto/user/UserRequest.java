package com.commonplant.umc.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequest {
    private String name;
    private String email;

    public UserRequest( String nickname, String email) {
        this.name = nickname;
        this.email = email;
    }
}

