package com.commonplant.umc.repository;

import com.commonplant.umc.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    Word findByWord(String word);

    @Modifying
    @Query("update Word p set p.searchedNumber = p.searchedNumber + 1 where p.wordIdx = ?1")
    int updateSearchedNumber(Long wordIdx);
}
