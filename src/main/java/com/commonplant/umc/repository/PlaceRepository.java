package com.commonplant.umc.repository;

import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByCode(String placeCode);



}
