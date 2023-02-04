package com.commonplant.umc.repository;

import com.commonplant.umc.domain.Belong;
import com.commonplant.umc.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BelongRepository extends JpaRepository<Belong, Long> {

}