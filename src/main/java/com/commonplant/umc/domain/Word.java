package com.commonplant.umc.domain;

import com.commonplant.umc.repository.WordRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "word")
@Entity
public class Word extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_idx")
    private Long wordIdx;

    @Column(nullable = false)
    private String word;

    @Column
    private Long searched_number;
}
