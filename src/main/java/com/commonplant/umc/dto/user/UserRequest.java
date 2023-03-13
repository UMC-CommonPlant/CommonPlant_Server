package com.commonplant.umc.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequest {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class join{
        private String email;
        private String nickName;
        private String loginType;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class update{
        private String email;
        private String nickName;
    }
}

