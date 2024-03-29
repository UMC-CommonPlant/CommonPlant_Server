package com.commonplant.umc.repository;

import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

    @Query(value = "SELECT p FROM Plant p WHERE p.place= ?1 "
            + "ORDER BY p.remainderDate DESC")
    List<Plant> findAllByPlaceOrderByRemainderDateDesc(Place place);

    @Query(value = "SELECT p FROM Plant p WHERE p.place= ?1 ")
    List<Plant> findAllByPlace(Place place);

    Plant findByPlantIdx(Long plantIdx);

    Long countByPlace(Place place);

    List<Plant> findAllByUser(User user);

    @Transactional
    void deleteAllByPlace(Place place);
}
