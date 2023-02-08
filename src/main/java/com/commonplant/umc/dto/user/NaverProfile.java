package com.commonplant.umc.dto.user;

import lombok.Data;

@Data
public class NaverProfile {
    private Long id;
    private String connected_at;
    private Properties properties;
    private NaverAccount naver_account;


    @Data
    public static class Properties{
        private String nickname;
        private String profile_image;
        private String thumbnail_image;
    }

    @Data
    public static class NaverAccount{
        private Boolean profile_needs_agreement;
        private Profile profile;
        private Boolean has_email;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;
    }

    @Data
    public static class Profile{
        private String nickname;
        private String profile_image;
        private boolean is_default_image;
    }
}
