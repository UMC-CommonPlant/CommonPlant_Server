package com.commonplant.umc.service;

import com.commonplant.umc.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WordService {
    private final WordRepository wordRepository;

    //@Transactional
    //public String addWord()
}
