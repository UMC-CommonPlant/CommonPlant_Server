package com.commonplant.umc.service;

import com.commonplant.umc.domain.Word;
import com.commonplant.umc.dto.word.WordResponse;
import com.commonplant.umc.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WordService {
    private final WordRepository wordRepository;

    @Transactional
    public void addWord(String name) {
        Word word = wordRepository.findByWord(name);
        if (word != null) {
            wordRepository.updateSearchedNumber(word.getWordIdx());
        } else {
            word = Word.builder().word(name).build();
            wordRepository.save(word);
        }
    }

    @Transactional
    public List<WordResponse.getWordInfo> getWordList() {
        List<Word> words = wordRepository.findAll(Sort.by(Sort.Direction.DESC, "searchedNumber"));
        List<WordResponse.getWordInfo> wordResponses = new ArrayList<>();
        for (Word w : words) {
            wordResponses.add(new WordResponse.getWordInfo(w));
        }

        return wordResponses;
    }
}
