package com.commonplant.umc.service;

import com.commonplant.umc.config.exception.BadRequestException;
import com.commonplant.umc.config.exception.ErrorResponseStatus;
import com.commonplant.umc.domain.Info;
import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.plant.PlantRequest;
import com.commonplant.umc.dto.plant.PlantResponse;
import com.commonplant.umc.repository.MemoRepository;
import com.commonplant.umc.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class PlantService {

    private final FirebaseService firebaseService;
    private final PlaceService placeService;
    private final InfoService infoService;
    private final PlantRepository plantRepository;
    private final MemoRepository memoRepository;



    @Transactional
    public Long addPlant(String name, String nickname, String place, String wateredDate, User user, MultipartFile file) {

        Info info = infoService.getPlantInfo(name);

        System.out.println("=============ADD PLANT INFO TO DATABASE===============");
        System.out.println("============= RequestParam(name): ===============" + name);

        // imgUrl Setter
        String newCode = null;
        String plantNickname = null;

        String imgUrl = null;

        // TODO: 식물의 애칭이 등록되어 있지 않거나 10자를 넘어갈 경우 예외처리
        if (nickname.length() == 0) {
            throw new BadRequestException(ErrorResponseStatus.NO_PLANT_NICKNAME);
        } else if (nickname.length() <= 10) {
            plantNickname = nickname;
        } else {
            throw new BadRequestException(ErrorResponseStatus.LONG_PLANT_NICKNAME);
        }

        // TODO: 식물 등록할 때 이미지가 없을 경우 예외처리
        if (file.getSize() > 0) {
            newCode = randomCode();
            imgUrl = firebaseService.uploadFiles("commonPlant_plant_" + newCode + "_" + newCode, file);
        } else {
            throw new BadRequestException(ErrorResponseStatus.NO_SELECTED_IMAGE);
        }

        Place plantPlace = placeService.getPlace(place);


        // TODO: 장소를 조회할 수 있는 유저만 식물이 등록 가능하게 하기
        if(!placeService.ExistUserInPlace(user, plantPlace)){
            throw new BadRequestException(ErrorResponseStatus.NOT_FOUND_USER_IN_PLACE);
        }

        // TODO: 마지막으로 물 준 날짜 - 날짜를 선택하지 않으면 등록일을 기준으로 설정
        LocalDateTime initialWateredDate = null;

        if (wateredDate == null) {
            // initialWateredDate = req.getCreatedAt().atStartOfDay(); // req.getCreatedAt() -> null 오류 발생
            initialWateredDate = LocalDate.now().atStartOfDay();
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            initialWateredDate = LocalDate.parse(wateredDate, dateTimeFormatter).atStartOfDay();
        }

        Plant plant = Plant.builder()
                .name(name)
                .user(user)
                .nickname(plantNickname)
                .place(plantPlace)
                .imgUrl(imgUrl)
                .wateredDate(initialWateredDate)
                .build();

        // remainderDate는 wateredDate를 활용해서 구해야 하기 때문에 동시에 저장되지 못함 (null 오류)
        // DateTimeFormatter
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // WateredDate: 마지막으로 물 준 날짜, CurrentDate: 오늘 날짜
        String parsedCurrentDate = LocalDate.now().toString();
        System.out.println("========parsedCurrentDate======== " + parsedCurrentDate);

        LocalDate currentDate = LocalDate.parse(parsedCurrentDate, dateTimeFormatter);
        LocalDateTime currentDateTime = currentDate.atStartOfDay();

        // remainderDate: D-DAY
        Long remainderDate = (Long) Duration.between(initialWateredDate, currentDateTime).toDays()
                - (Long) info.getWater_day();

        plant.setRemainderDate(remainderDate);

        return plantRepository.save(plant).getPlantIdx();
    }

    // getplantCard(): 식물 1개의 정보를 얻어옴
    // 함께한지 1일이 지났어요!: getCreatedAt() 활용
    // Info에서 불러와야 할 정보: name, scientific_name, water_day
    @Transactional
    public PlantResponse.plantCardRes getPlantCard(Long plantIdx, User user) throws ExecutionException, InterruptedException {

        Plant plant = plantRepository.findByPlantIdx(plantIdx);

        Place place = plant.getPlace();
        System.out.println("========== 식물이 등록된 장소는: ==========" + place);

        // TODO: 장소를 조회할 수 있는 유저만 등록한 식물 정보를 조회 가능하게 하기
        if(!placeService.ExistUserInPlace(user, place)){
            throw new BadRequestException(ErrorResponseStatus.NOT_FOUND_USER_IN_PLACE);
        }

        System.out.println(plant.getName());

        // getPlantInfo()의 인자는 식물 종 이름: 식물 조회할 때 식물 종 이름을 보내서 검색하면 됨
        Info info = infoService.getPlantInfo(plant.getName());

        System.out.println("=============GET PLANT INFO FROM FIREBASE===============");

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

        LocalDate wateredDate = LocalDate.parse(parsedWateredDate, dateTimeFormatter);
        LocalDate currentDate = LocalDate.parse(parsedCurrentDate, dateTimeFormatter);
        LocalDate createdDate = LocalDate.parse(parsedCreatedDate, dateTimeFormatter);
        LocalDateTime wateredDateTime = wateredDate.atStartOfDay();
        LocalDateTime currentDateTime = currentDate.atStartOfDay();
        LocalDateTime createdDateTime = createdDate.atStartOfDay();

        // remainderDate: 물주기까지 남은 날짜 (-> addPlant로 이동)
        // Long remainderDate = (Long) info.getWater_day() - (Long) Duration.between(wateredDateTime, currentDateTime).toDays();
        // plant.setRemainderDate(remainderDate);

        System.out.println(currentDate);
        System.out.println(wateredDate);
        System.out.println(plant.getRemainderDate());

        // countDate: 식물이 처음 온 날
        Long countDate =  (Long) Duration.between(createdDateTime, currentDateTime).toDays() + 1;

        PlantResponse.plantCardRes testRes = new PlantResponse.plantCardRes(
                plant.getPlantIdx(),
                plant.getName(),
                plant.getNickname(),
                plant.getPlace().getName(),
                plant.getImgUrl(),
                countDate,
                plant.getRemainderDate(),
                // remainderDate,
                info.getScientific_name(),
                info.getWater_day(),
                info.getSunlight(),
                info.getTemp_min(),
                info.getTemp_max(),
                info.getHumidity(),
                plant.getCreatedAt(),
                plant.getWateredDate()
        );

        return testRes;
    }


    @Transactional
    public List<Plant> getPlantList(Place place) {
        List<Plant> plants = plantRepository.findAllByPlaceOrderByRemainderDateDesc(place);

        return plants;
    }

    @Transactional
    public void deleteAllMemo(Plant plant){

    }

    @Transactional
    public Plant getPlant(Long plantIdx) {
        return plantRepository.findByPlantIdx(plantIdx);
    }


    @Transactional
    public Long updatePlant(Long plantIdx, String nickname, User user, MultipartFile file) {

        Place place = getPlant(plantIdx).getPlace();

        // TODO: 장소를 조회할 수 있는 유저만 식물을 수정 가능하게 하기
        if (!placeService.ExistUserInPlace(user, place)) {
            throw new BadRequestException(ErrorResponseStatus.NOT_FOUND_USER_IN_PLACE);
        }

        Plant plant = plantRepository.findByPlantIdx(plantIdx);

        String plantNickname = null;

        // TODO: 식물의 애칭이 등록되어 있지 않거나 10자를 넘어갈 경우 예외처리
        // TODO: 수정 전 닉네임과 수정 후 닉네임이 같을 경우?
        if (nickname.length() == 0) {
            throw new BadRequestException(ErrorResponseStatus.NO_PLANT_NICKNAME);
        } else if (nickname.length() <= 10) {
            plantNickname = nickname;
        } else {
            throw new BadRequestException(ErrorResponseStatus.LONG_PLANT_NICKNAME);
        }

        // imgUrl Setter
        // 이미지가 바뀌더라도 url은 똑같게!
        String imgUrl = null;

        if (file.getSize() > 0) {
            imgUrl = plant.getImgUrl();
            // imgUrl = firebaseService.uploadFiles(imgUrl, file);
        } else {
            throw new BadRequestException(ErrorResponseStatus.NO_SELECTED_IMAGE);
        }

        // updatePlant
        plant.updatePlant(
                // nickname,
                plantNickname,
                imgUrl
        );

        return plantRepository.save(plant).getPlantIdx();
    }

    @Transactional
    public String updateWateredDate(Long plantIdx, User user) {

        Plant plant = plantRepository.findByPlantIdx(plantIdx);
        System.out.println(" 물주기 리셋할 식물은: " + plantIdx);

        Place place = plant.getPlace();
        System.out.println("========== 식물이 등록된 장소는: ==========" + place);

        // TODO: 장소를 조회할 수 있는 유저만 식물 물주기 업데이트 가능
        if(!placeService.ExistUserInPlace(user, place)){
            throw new BadRequestException(ErrorResponseStatus.NOT_FOUND_USER_IN_PLACE);
        }

        // getPlantInfo()의 인자는 식물 종 이름: 식물 조회할 때 식물 종 이름을 보내서 검색하면 됨
        Info info = infoService.getPlantInfo(plant.getName());

        plant.setWateredDate(
                plant.getWateredDate()
        );

        Long resetRemainderDate = -1 * (Long) info.getWater_day();

        plant.setRemainderDate(
                resetRemainderDate
        );

        String updateWateredDateTest = " 마지막으로 식물에 물을 준 날짜: " + plant.getWateredDate();
        System.out.println(updateWateredDateTest);

        return updateWateredDateTest;
    }

    @Transactional
    public Long deletePlant(Long plantIdx, User user) {

        Plant plant = plantRepository.findByPlantIdx(plantIdx);

        // TODO: 식물을 등록한 사람과 삭제하려는 사람이 같은지 비교
        User writer = plant.getUser();

        if (writer != user) {
            throw new BadRequestException(ErrorResponseStatus.INVALID_USER_JWT);
        }

        // TODO: 식물에 작성된 메모를 먼저 삭제하고 식물을 삭제
        memoRepository.deleteAllByPlantIdx(plantIdx);

        plantRepository.deleteById(plantIdx);

        return plantIdx;
    }

    public String randomCode() {
        return RandomStringUtils.random(6,33,125,true,false);
    }
}
