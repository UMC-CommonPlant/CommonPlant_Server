package com.commonplant.umc.dto.memo;

import lombok.Data;

public class MemoRequest {

    @Data
    public static class addMemo {
        private Long plant;
        private String content;
    }

    @Data
    public static class updateMemo {
//        private Long plant;
//        private Long writer;
        private String content;
    }
}

