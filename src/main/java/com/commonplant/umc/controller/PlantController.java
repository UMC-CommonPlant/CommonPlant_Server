package com.commonplant.umc.controller;

import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.plant.PlantRequest;
import com.commonplant.umc.service.PlantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

        System.out.println("=============TEST.NAME===============" + req.getName());

        String test = plantService.addPlant(req);

        return ResponseEntity.ok(new JsonResponse(true, 200, "addPlant", test));
    }
//    public String createPlant(Plant plant) {
//        plantService.createPlant(plant);
//
//        return " ";
//    }

    // 식물 조회
//    @GetMapping("/plant/card/{plantIdx}")
//    public String plantCard() {
//
//    }

}
