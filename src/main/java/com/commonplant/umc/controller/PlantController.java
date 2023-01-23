package com.commonplant.umc.controller;

import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.plant.PlantRequest;
import com.commonplant.umc.dto.plant.PlantResponse;
import com.commonplant.umc.service.PlantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    // 식물 등록 (GET): 상품 등록을 완료하면 등록된 화면을 띄움
    @GetMapping("/plant/new")
    public String newPlant() {
        return "plant/new";
    }

    // 식물 등록 (POST)
    @PostMapping("/plant/add")
    public ResponseEntity<JsonResponse> addPlant(@RequestBody PlantRequest.addPlant req){

        System.out.println("=============ADD PLANT TEST.NAME===============" + req.getName());

        String test = plantService.addPlant(req);

        return ResponseEntity.ok(new JsonResponse(true, 200, "addPlant", test));
    }

    // 식물 수정 (PATCH)
    @PatchMapping("/plant/update/{plantIdx}")
    public ResponseEntity<JsonResponse> updatePlant(@PathVariable Long plantIdx, @RequestBody PlantRequest.updatePlant req){

        System.out.println("=============UPDATE PLANT TEST.NAME===============" + req.getName());

        String updatePlantTest = plantService.updatePlant(plantIdx, req);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updatePlant", updatePlantTest));
    }

    // 식물 조회 (GET)
    @GetMapping("/plant/card/{plantIdx}")
    public ResponseEntity<JsonResponse> getPlantCard(@PathVariable Long plantIdx) {

        System.out.println("=============GET PLANT TEST.NAME===============");

        PlantResponse.plantCardRes res = plantService.getPlantCard(plantIdx);

        return ResponseEntity.ok(new JsonResponse(true,200, "getPlantCard", res));
    }
}
