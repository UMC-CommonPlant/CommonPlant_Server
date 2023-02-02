package com.commonplant.umc.service;

import com.commonplant.umc.domain.Info;
import com.commonplant.umc.domain.Word;
import com.commonplant.umc.dto.word.WordResponse;
import com.commonplant.umc.repository.WordRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.commonplant.umc.service.InfoService.COLLECTION_NAME;

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

            try {
                Firestore firestore = FirestoreClient.getFirestore();
                DocumentReference documentReference = firestore.collection(COLLECTION_NAME).document(name);
                ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
                DocumentSnapshot documentSnapshot = apiFuture.get();
                if (documentSnapshot.exists()) {
                    Info info = documentSnapshot.toObject(Info.class);
                    String imgUrl = info.getImgUrl();
                    String scientific_name = info.getScientific_name();
                    word = Word.builder().word(name).imgUrl(imgUrl).scientific_name(scientific_name).build();
                    wordRepository.save(word);
                }
            } catch (ExecutionException e) {
                System.out.println("ExecutionException");
            } catch (InterruptedException e) {
                System.out.println("InterruptedException");
            }
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
