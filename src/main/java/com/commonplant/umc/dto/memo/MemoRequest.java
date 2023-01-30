package com.commonplant.umc.dto.memo;

import com.commonplant.umc.domain.Plant;
import lombok.Data;

import java.time.LocalDate;

public class MemoRequest {

    @Data
    public static class addMemo {
        private Plant plant;
        private String user;
        private String content;
        private LocalDate createdAt;
    }

    @Data
    public static class updateMemo {
        private Plant plant;
        private String user;
        private String content;
    }
}

