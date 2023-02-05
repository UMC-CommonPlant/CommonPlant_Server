package com.commonplant.umc.controller;

import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.memo.MemoResponse;
import com.commonplant.umc.dto.plant.PlantRequest;
import com.commonplant.umc.dto.plant.PlantResponse;
import com.commonplant.umc.service.MemoService;
import com.commonplant.umc.service.PlantService;
import com.commonplant.umc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;
    private final JwtService jwtService;
    private final UserService userService;
    private final MemoService memoService;

    // 식물 등록 (GET): 상품 등록을 완료하면 등록된 화면을 띄움
    @GetMapping("/plant/new")
    public String newPlant() {
        return "plant/new";
    }

    // 식물 등록 (POST)
    @PostMapping("/plant/add")
    public ResponseEntity<JsonResponse> addPlant(@RequestPart("plant") PlantRequest.addPlant req, @RequestPart("image") MultipartFile file)
            throws ExecutionException, InterruptedException {

        System.out.println("=============ADD PLANT TEST.NAME===============" + req.getNickname());
        System.out.println("=============ADD PLANT TEST.NAME===============" + req.getPlace());
        System.out.println("=============ADD PLANT TEST.NAME===============" + file);
        System.out.println("=============ADD PLANT TEST.NAME===============" + req.getCreatedAt());

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        Long test = plantService.addPlant(req, user,file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "addPlant", test));
    }

    // 식물 수정 (PATCH)
    @PutMapping("/plant/update/{plantIdx}")
    public ResponseEntity<JsonResponse> updatePlant(@PathVariable Long plantIdx,
                                                    @RequestPart("plant") PlantRequest.updatePlant req,
                                                    @RequestPart("image") MultipartFile file){

        System.out.println("=============UPDATE PLANT TEST.NAME===============" + req.getNickname());
        System.out.println("=============UPDATE PLANT TEST.NAME===============" + file);

        String updatePlantTest = plantService.updatePlant(plantIdx, req, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updatePlant", updatePlantTest));
    }

    // 식물 물주기 날짜 갱신(PATCH)
    @PutMapping("/plant/update/wateredDate/{plantIdx}")
    public ResponseEntity<JsonResponse> updateWateredDate(@PathVariable Long plantIdx,
                                                          @RequestPart("plant") PlantRequest.updateWateredDate req,
                                                          @RequestPart("image") MultipartFile file){

        System.out.println("=============UPDATE PLANT WATERED DATE TEST.NAME===============");

        String uuid = jwtService.resolveToken();

        String updateWateredDateTest = plantService.updateWateredDate(plantIdx, req, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updateWateredDate", updateWateredDateTest));
    }

    // 식물 조회 (GET)
    // memo list 까지 response로 줄 수 있게 구현 완료!
    @GetMapping("/plant/card/{plantIdx}")
    public ResponseEntity<JsonResponse> getPlantCard(@PathVariable Long plantIdx)
            throws ExecutionException, InterruptedException {

        String uuid = jwtService.resolveToken();

        System.out.println("=============GET PLANT TEST.NAME===============");

        PlantResponse.plantCardRes plant = plantService.getPlantCard(plantIdx);
        MemoResponse.memoListRes memoList = memoService.getMemoList(plant.getPlantIdx());
        System.out.println(memoList);

        return ResponseEntity.ok(new JsonResponse(true,200, "getPlantCard",
                new PlantResponse.plantAndMemoRes(plant, memoList)));
    }

    // 테스트 (GET)
    @GetMapping("/plant/test/plantIdx")
    public ResponseEntity<JsonResponse> getPlant() {

        System.out.println("=============GET PLANT TEST.NAME===============");

        Plant res = plantService.getPlant(1L);
        System.out.println(res.getPlantIdx());

        return ResponseEntity.ok(new JsonResponse(true,200, "getPlant", res));
    }
}
