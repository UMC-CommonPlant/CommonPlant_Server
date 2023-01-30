package com.commonplant.umc.service;

import com.commonplant.umc.domain.Word;
import com.commonplant.umc.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WordService {
    private final WordRepository wordRepository;

    @Transactional
    public void addWord(String name){
        Word word = wordRepository.findByWord(name);
        if(word != null){
            wordRepository.updateSearchedNumber(word.getWordIdx());
            System.out.println(word.getWord());
        }
        else{
            word = Word.builder().word(name).build();
            wordRepository.save(word);
        }
    }
}
