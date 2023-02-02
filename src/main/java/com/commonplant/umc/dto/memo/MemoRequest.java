package com.commonplant.umc.dto.memo;

import lombok.Data;

import java.time.LocalDate;

public class MemoRequest {

    @Data
    public static class addMemo {
        private Long plant;
        private String user;
        private String content;
        private LocalDate createdAt;
    }

    @Data
    public static class updateMemo {
        private Long plant;
        private String user;
        private String content;
    }
}

