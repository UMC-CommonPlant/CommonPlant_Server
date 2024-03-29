package com.commonplant.umc.controller;

import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.memo.MemoResponse;
import com.commonplant.umc.dto.plant.PlantRequest;
import com.commonplant.umc.dto.plant.PlantResponse;
import com.commonplant.umc.service.MemoService;
import com.commonplant.umc.service.PlaceService;
import com.commonplant.umc.service.PlantService;
import com.commonplant.umc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;
    private final JwtService jwtService;
    private final UserService userService;
    private final MemoService memoService;

    private final PlaceService placeService;

    // 식물 등록 (GET): 상품 등록을 완료하면 등록된 화면을 띄움
    @GetMapping("/plant/new")
    public String newPlant() {
        return "plant/new";
    }

    // 식물 등록 (POST)
    // TODO: @RequestPart to @RequestParam
    @PostMapping("/plant/add")
    public ResponseEntity<JsonResponse> addPlant(@RequestParam("name") String name,
                                                 @RequestParam("nickname") String nickname,
                                                 @RequestParam("place") String place,
                                                 @RequestParam("wateredDate") String wateredDate,
                                                 @RequestPart("image") MultipartFile file)
            throws ExecutionException, InterruptedException {

        System.out.println("=============ADD PLANT TEST.NAME===============" + name);
        System.out.println("=============ADD PLANT TEST.NAME===============" + nickname);
        System.out.println("=============ADD PLANT TEST.NAME===============" + place);
        System.out.println("=============ADD PLANT TEST.NAME===============" + wateredDate);

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        Long test = plantService.addPlant(name, nickname, place, wateredDate, user, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "addPlant", test));
    }

    // 식물 수정 (PUT)
    // TODO: @RequestPart to @RequestParam
    @PutMapping("/plant/update/{plantIdx}")
    public ResponseEntity<JsonResponse> updatePlant(@PathVariable Long plantIdx,
                                                    @RequestParam("nickname") String nickname,
                                                    @RequestPart("image") MultipartFile file){

        System.out.println("=============UPDATE PLANT TEST.NAME===============" + nickname);
        System.out.println("=============UPDATE PLANT TEST.NAME===============" + file);

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        Long updatePlantTest = plantService.updatePlant(plantIdx, nickname, user, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updatePlant", updatePlantTest));
    }

    // 식물 물주기 날짜 갱신 (PUT)
    @PutMapping("/plant/update/wateredDate/{plantIdx}")
    public ResponseEntity<JsonResponse> updateWateredDate(@PathVariable Long plantIdx){

        System.out.println("=============UPDATE PLANT WATERED DATE TEST.NAME===============");

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        String updateWateredDateTest = plantService.updateWateredDate(plantIdx, user);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updateWateredDate", updateWateredDateTest));
    }

    // 식물 조회 (GET)
    // memo list 까지 response로 줄 수 있게 구현 완료!
    @GetMapping("/plant/card/{plantIdx}")
    public ResponseEntity<JsonResponse> getPlantCard(@PathVariable Long plantIdx)
            throws ExecutionException, InterruptedException {

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        System.out.println("=============GET PLANT TEST.NAME===============");

        PlantResponse.plantCardRes plant = plantService.getPlantCard(plantIdx, user);
        MemoResponse.memoListRes memoList = memoService.getMemoList(plant.getPlantIdx(), user);
        System.out.println(memoList);

        return ResponseEntity.ok(new JsonResponse(true,200, "getPlantCard",
                new PlantResponse.plantAndMemoRes(plant, memoList)));
    }


//     TODO: 같은 장소에 있는 식물 리스트 조회 (GET)
//     장소 별로 조회... place 테이블의 place_idx 사용?
    @GetMapping("/plant/{placeCode}/plantList")
    public ResponseEntity<JsonResponse> getPlantList(@PathVariable String placeCode)
            throws ExecutionException, InterruptedException {

        Place place = placeService.getPlace(placeCode);

        System.out.println("=============GET PLANT LIST TEST.NAME===============");

        List<Plant> plants = plantService.getPlantList(place);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getPlantList", plants));
    }

    // 테스트 (GET)
    @GetMapping("/plant/test/plantIdx")
    public ResponseEntity<JsonResponse> getPlant() {

        System.out.println("=============GET PLANT TEST.NAME===============");

        Plant res = plantService.getPlant(1L);
        System.out.println(res.getPlantIdx());

        return ResponseEntity.ok(new JsonResponse(true,200, "getPlant", res));
    }

    // 식물 삭제
    @DeleteMapping("/plant/delete/{plantIdx}")
    public ResponseEntity deletePlant(@PathVariable Long plantIdx){

        System.out.println("=============DELETE PLANT TEST.NAME==============");

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        Long deletePlantTest = plantService.deletePlant(plantIdx, user);

        return ResponseEntity.ok(new JsonResponse(true, 200, "deletePlant", deletePlantTest));
    }
}
