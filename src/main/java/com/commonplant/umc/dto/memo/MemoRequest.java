package com.commonplant.umc.dto.memo;

import com.commonplant.umc.domain.Plant;
import lombok.Data;

public class MemoRequest {

    @Data
    public static class addMemo {
        private Plant plant;
        private String user;
        private String content;
    }

    @Data
    public static class updateMemo {
        private Plant plant;
        private String user;
        private String content;
    }
}
