package com.commonplant.umc.controller;

import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.plant.PlantRequest;
import com.commonplant.umc.dto.plant.PlantResponse;
import com.commonplant.umc.service.PlantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<JsonResponse> addPlant(@RequestPart("plant") PlantRequest.addPlant req, @RequestPart("image") MultipartFile file){

        System.out.println("=============ADD PLANT TEST.NAME===============" + req.getName());
        System.out.println("=============ADD PLANT TEST.NAME===============" + file);
        System.out.println("=============ADD PLANT TEST.NAME===============" + req.getCreatedAt());

        String test = plantService.addPlant(req, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "addPlant", test));
    }

    // 식물 수정 (PATCH)
    @PatchMapping("/plant/update/{plantIdx}")
    public ResponseEntity<JsonResponse> updatePlant(@PathVariable Long plantIdx,
                                                    @RequestPart("plant") PlantRequest.updatePlant req,
                                                    @RequestPart("image") MultipartFile file){

        System.out.println("=============UPDATE PLANT TEST.NAME===============" + req.getName());
        System.out.println("=============UPDATE PLANT TEST.NAME===============" + file);
        // System.out.println("=============UPDATE PLANT TEST.NAME===============" + req.getCreatedAt());

        String updatePlantTest = plantService.updatePlant(plantIdx, req, file);

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
