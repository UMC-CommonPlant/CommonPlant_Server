package com.commonplant.umc.service;

import com.commonplant.umc.domain.Info;
import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.plant.PlantRequest;
import com.commonplant.umc.dto.plant.PlantResponse;
import com.commonplant.umc.repository.PlaceRepository;
import com.commonplant.umc.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlantService {

    private final FirebaseService firebaseService;

    private final PlaceService placeService;

    private final InfoService infoService;

    private final PlantRepository plantRepository;
    private final PlaceRepository placeRepository;


    @Transactional
    public Long addPlant(PlantRequest.addPlant req, User user, MultipartFile file)  {

        Info info = infoService.getPlantInfo(req.getName());

        System.out.println("=============ADD PLANT INFO TO DATABASE===============");
        System.out.println("============= req.getName(): ===============" + req.getName());
        // System.out.println("============= info.getName(): ===============" + info.getName());

        // imgUrl Setter
        String newCode = randomCode();
        String nickname = req.getNickname();

        String imgUrl = null;

        if (file.getSize() > 0) {
            imgUrl = firebaseService.uploadFiles("commonPlant_plant/" + nickname + "_" + newCode, file);
        }

        Place place = placeService.getPlace(req.getPlace());

        Plant plant = Plant.builder()
                .name(info.getName())
                .user(user)
                .nickname(req.getNickname())
                .place(place)
                .imgUrl(imgUrl)
                .wateredDate(req.getWateredDate())
                .build();

//        plantRepository.save(plant);

//        String test = " 식물 이름: " + plant.getName()
//                + " 식물 애칭: " + plant.getNickname() + " 이미지 url: " + plant.getImgUrl()
//                + " 식물과 함께하기 시작한 날: " + plant.getCreatedAt()
//                + " 식물에 마지막으로 물을 준 날: " + plant.getWateredDate();
//
//        return test;

        return plantRepository.save(plant).getPlantIdx();
    }

    // getplantCard(): 식물 1개의 정보를 얻어옴
    // 함께한지 1일이 지났어요!: getCreatedAt() 활용
    // Info에서 불러와야 할 정보: name, scientific_name, water_day
    @Transactional
    public PlantResponse.plantCardRes getPlantCard(Long plantIdx) throws ExecutionException, InterruptedException {

        Plant plant = plantRepository.findByPlantIdx(plantIdx);

        System.out.println(plant.getName());

        // getPlantInfo()의 인자는 식물 종 이름: 식물 조회할 때 식물 종 이름을 보내서 검색하면 됨
        Info info = infoService.getPlantInfo(plant.getName());

        System.out.println("=============GET PLANT INFO FROM FIREBASE===============");
        System.out.println(info.getName());

        System.out.println(info.getImgUrl());
        System.out.println(info.getScientific_name());
        //water_day: Long
        System.out.println(info.getWater_day());

        System.out.println(info.getSunlight());
        System.out.println(info.getTemp_min());
        System.out.println(info.getTemp_max());
        System.out.println(info.getHumidity());

        // 물주기 구현: 남은 날짜 계산
        // DateTimeFormatter
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // WateredDate: 마지막으로 물 준 날짜, CurrentDate: 오늘 날짜
        String parsedWateredDate = plant.getWateredDate().format(dateTimeFormatter);
        System.out.println("========parsedWateredDate======== " + parsedWateredDate);
        String parsedCurrentDate = LocalDate.now().toString();
        System.out.println("========parsedCurrentDate======== " + parsedCurrentDate);
        String parsedCreatedDate = plant.getCreatedAt().format(dateTimeFormatter);
        System.out.println("========parsedCreatedDate======== " + parsedCreatedDate);


        // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate wateredDate = LocalDate.parse(parsedWateredDate, dateTimeFormatter);
        LocalDate currentDate = LocalDate.parse(parsedCurrentDate, dateTimeFormatter);
        LocalDate createdDate = LocalDate.parse(parsedCreatedDate, dateTimeFormatter);
        LocalDateTime wateredDateTime = wateredDate.atStartOfDay();
        LocalDateTime currentDateTime = currentDate.atStartOfDay();
        LocalDateTime createdDateTime = createdDate.atStartOfDay();

        // remainderDate: 물주기까지 남은 날짜
        Long remainderDate = (Long) info.getWater_day() - (Long) Duration.between(wateredDateTime, currentDateTime).toDays();
        plant.setRemainderDate(remainderDate);


        System.out.println(currentDate);
        System.out.println(wateredDate);
        System.out.println(remainderDate);

        // countDate: 식물이 처음 온 날
        Long countDate =  (Long) Duration.between(createdDateTime, currentDateTime).toDays();

        PlantResponse.plantCardRes testRes = new PlantResponse.plantCardRes(
                plant.getPlantIdx(),
                plant.getName(),
                plant.getNickname(),
                plant.getPlace(),
                plant.getImgUrl(),
                countDate,
                plant.getCreatedAt(),
                plant.getWateredDate()
        );

        return testRes;
    }


    @Transactional
    public List<Plant> getPlantList(Place place)
    {
        List<Plant> plants = plantRepository.findAllByPlaceOOrderByRemainderDateAsc(place);

        return plants;
    }

    @Transactional
    public Plant getPlant(Long plantIdx){
        return plantRepository.findByPlantIdx(plantIdx);
    }


    @Transactional
    public String updatePlant(Long plantIdx, PlantRequest.updatePlant req, MultipartFile file){

        // imgUrl Setter
        String newCode = randomCode();
        String nickname = req.getNickname();

        String imgUrl = null;

        if (file.getSize() > 0) {
            imgUrl = firebaseService.uploadFiles("commonPlant_plant/" + nickname + "_" + newCode, file);
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

    @Transactional
    public String updateWateredDate(Long plantIdx, PlantRequest.updateWateredDate req, MultipartFile file){

        Plant plant = plantRepository.findByPlantIdx(plantIdx);
        System.out.println(" 물주기 리셋할 식물은: " + plantIdx);

        plant.setWateredDate(
                req.getWateredDate()
        );

        String updateWateredDateTest = " 마지막으로 식물에 물을 준 날짜: " + plant.getWateredDate();
        System.out.println(updateWateredDateTest);

        return updateWateredDateTest;
    }

    public String randomCode(){
        return RandomStringUtils.random(6,33,125,true,false);
    }
}
