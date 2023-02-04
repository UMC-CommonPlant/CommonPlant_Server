package com.commonplant.umc.repository;

import com.commonplant.umc.domain.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

    Plant findByPlantIdx(Long plantIdx);
}
