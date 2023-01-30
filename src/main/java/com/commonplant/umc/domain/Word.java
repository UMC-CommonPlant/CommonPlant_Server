package com.commonplant.umc.domain;

import com.commonplant.umc.repository.WordRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(nullable = false, unique = true)
    private String word;

    @Column
    private Long searched_number;

    @PrePersist
    public void prePersist(){
        this.searched_number = this.searched_number==null? 1L : this.searched_number;
    }
}
