package com.commonplant.umc.repository;

import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

//    // SELECT * FROM plant WHERE place_idx = (식물 리스트를 보고자하는 장소의 place_idx) ORDER BY cast(p.remainderDate as unsigned);
    @Query(value = "SELECT p FROM Plant p WHERE p.place= ?1 ")
    List<Plant> findAllByPlaceOOrderByRemainderDateAsc(Place place);


//  List<Plant> findAllByPlaceIdxOrderByRemainderDate(Long placeIdx);

    Plant findByPlantIdx(Long plantIdx);
}
