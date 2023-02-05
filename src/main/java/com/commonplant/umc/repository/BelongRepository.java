package com.commonplant.umc.repository;

import com.commonplant.umc.domain.Belong;
import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BelongRepository extends JpaRepository<Belong, Long> {

    @Query("SELECT m.place FROM Belong m WHERE m.user=?1")
    List<Place> findAllByUserOrdOrderByCreatedAtAsc(User user);
}