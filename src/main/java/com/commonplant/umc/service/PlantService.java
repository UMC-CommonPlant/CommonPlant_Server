package com.commonplant.umc.service;

import com.commonplant.umc.domain.Info;
import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.dto.plant.PlantRequest;
import com.commonplant.umc.dto.plant.PlantResponse;
import com.commonplant.umc.repository.PlantRepository;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class PlantService {

    private final FirebaseService firebaseService;

    private final PlaceService placeService;

    private final InfoService infoService;

    private final PlantRepository plantRepository;

    @Transactional
    public String addPlant(PlantRequest.addPlant req, MultipartFile file){

        String name = req.getNickname();

        String imgUrl = null;

        if(file.getSize()>0){
            imgUrl = firebaseService.uploadFiles("commonPlant_" + name,file);
        }

        Place place = placeService.getPlace(req.getPlace());

        Plant plant = Plant.builder()
                .name(req.getName())
                .nickname(req.getNickname())
                .place(place)
                .imgUrl(imgUrl)
                .wateredDate(req.getWateredDate())
                .build();

        plantRepository.save(plant);

        String test = " 식물 애칭: " + plant.getNickname() + " 이미지 url: " + plant.getImgUrl()
                    + " 식물과 함께하기 시작한 날: " + plant.getCreatedAt()
                    + " 식물에 마지막으로 물을 준 날: " + plant.getWateredDate();

        return test;
    }

    // getplantCard(): 식물 1개의 정보를 얻어옴
    // 함께한지 1일이 지났어요!: getCreatedAt() 활용
    // Info에서 불러와야 할 정보: name, scientific_name, water_day
    @Transactional
    public PlantResponse.plantCardRes getPlantCard(Long plantIdx) throws ExecutionException, InterruptedException {

        Plant plant = plantRepository.findByPlantIdx(plantIdx);

        PlantResponse.plantCardRes testRes = new PlantResponse.plantCardRes(
                plant.getPlantIdx(),
                plant.getName(),
                plant.getNickname(),
                plant.getPlace(),
                plant.getImgUrl(),
                plant.getCreatedAt(),
                plant.getWateredDate()
        );

        return testRes;
    }

    @Transactional
    public Plant getPlant(Long plantIdx){
        return plantRepository.findByPlantIdx(plantIdx);
    }


    @Transactional
    public String updatePlant(Long plantIdx, PlantRequest.updatePlant req, MultipartFile file){

        String name = req.getNickname();

        String imgUrl = null;

        if(file.getSize()>0){
            imgUrl = firebaseService.uploadFiles("commonPlant_" + name,file);
        }

        Plant plant = plantRepository.findByPlantIdx(plantIdx);

        plant.updatePlant(
                req.getNickname(),
                imgUrl,
                req.getWateredDate()
        );

        String updatePlantTest = " 식물 애칭: " + req.getNickname() + " 수정된 장소: " + req.getPlace()
                + " 수정된 이미지 url: " + plant.getImgUrl()
                + " 식물과 함께하기 시작한 날: " + plant.getCreatedAt();

        System.out.println(updatePlantTest);

        return updatePlantTest;
    }
}
