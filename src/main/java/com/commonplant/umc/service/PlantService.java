package com.commonplant.umc.service;

import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.dto.plant.PlantRequest;
import com.commonplant.umc.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlantService {

    private final PlantRepository plantRepository;

    @Transactional
    public String addPlant(PlantRequest.addPlant req){

        String imgUrl = " ";

        Plant plant = Plant.builder()
                .name(req.getName())
                .place(req.getPlace())
                .imgUrl(imgUrl)
                .build();

        plantRepository.save(plant);

        String test = " 식물 애칭: " + plant.getName() + " 이미지 url: " + plant.getImgUrl();

        return test;
    }

    @Transactional
    public String updatePlant(Long plantIdx, PlantRequest.updatePlant req){

        Plant plant = plantRepository.findByPlantIdx(plantIdx);

        plant.updatePlant(
                req.getName(),
                req.getPlace(),
                req.getImgUrl()
        );

        String updatePlantTest = " 식물 애칭: " + req.getName() + " 수정된 장소: " + req.getPlace();

        System.out.println(updatePlantTest);

        return updatePlantTest;
    }

//    public Plant readPlant(Plant plant){
//        Plant entity = plantRepository.findById(plant.getPlantIdx()).orElseThrow(
//                () -> new IllegalArgumentException("식물이 존재하지 않습니다.")
//        );
//
//        return new Plant(entity);
//    }

//
//    public void deletePlant(Long plantIdx){
//        Plant plant = plantRepository.findById(plantIdx).orElseThrow(
//                () -> new IllegalArgumentException("등록된 식물이 없어 삭제할 수 없습니다.")
//        );
//
//        plantRepository.delete(plant);
//    }

}
