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
    private final FirebaseService firebaseService;

    @Transactional
    public void addWord(String name) {
        Word word = wordRepository.findByWord(name);
        if (word != null) {
            wordRepository.updateSearchedNumber(word.getWordIdx());
        } else {
            //한국어일 경우, 영어일 경우 분리
            //근데 그 한국어랑 영어랑 같은 데이터를 가리키고 있다면?

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
