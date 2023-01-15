package com.commonplant.umc.service;

import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.dto.plant.PlantRequest;
import com.commonplant.umc.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlantService {

    // @Autowired의 default required 값은 true인데, true로 하면 error 발생
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

        String test = "식물 애칭: " + plant.getName() + "이미지 url: " + plant.getImgUrl();

        return test;
    }

}
