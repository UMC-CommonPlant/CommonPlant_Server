package com.commonplant.umc.dto.word;

import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.Word;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WordResponse {

    @NoArgsConstructor
    @Data
    public static class getWordInfo{
        private String name;
        private Long searchedNumber;

        @Builder
        public getWordInfo(Word word){
            this.name = word.getWord();
            this.searchedNumber = word.getSearchedNumber();
        }
    }
}
