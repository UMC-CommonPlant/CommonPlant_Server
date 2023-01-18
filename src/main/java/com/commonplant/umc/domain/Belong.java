package com.commonplant.umc.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Table(name = "belong")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Belong extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "belong_idx")
    private Long belongIdx;

    @ManyToOne
    @JoinColumn(name = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "place")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;

    @Builder
    public Belong(User user, Place place) {
        this.user = user;
        this.place = place;
    }
}