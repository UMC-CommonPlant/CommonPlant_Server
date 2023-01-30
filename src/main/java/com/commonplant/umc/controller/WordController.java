package com.commonplant.umc.controller;

import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.word.WordResponse;
import com.commonplant.umc.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @GetMapping("/word/getWordList")
    public ResponseEntity<JsonResponse> getWordList(){
        List<WordResponse.getWordInfo> wordInfo = wordService.getWordList();
        return ResponseEntity.ok(new JsonResponse(true, 200, "wordInfo", wordInfo));
    }
}
