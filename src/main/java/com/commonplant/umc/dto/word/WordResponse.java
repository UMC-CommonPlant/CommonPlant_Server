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
        private String imgUrl;
        private String scientific_name;

        @Builder
        public getWordInfo(Word word){
            this.name = word.getWord();
            this.searchedNumber = word.getSearchedNumber();
            this.imgUrl = word.getImgUrl();
            this.scientific_name = word.getScientific_name();
        }
    }
}
