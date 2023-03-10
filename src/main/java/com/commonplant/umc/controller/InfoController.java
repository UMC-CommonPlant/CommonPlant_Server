package com.commonplant.umc.controller;


import com.commonplant.umc.domain.Info;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.info.InfoResponse;
import com.commonplant.umc.service.InfoService;
import com.commonplant.umc.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;
    private final WordService wordService;

    @PostMapping("/info/searchInfo")
    public ResponseEntity<JsonResponse> searchInfo(@RequestParam String name) {
        List<InfoResponse.getSearchList> searchInfo = infoService.searchInfo(name);
        return ResponseEntity.ok(new JsonResponse(true, 200, "searchInfo", searchInfo));
    }

    @PostMapping("/info/getPlantInfo")
    public ResponseEntity<JsonResponse> getPlantInfo(@RequestParam String name) {
        wordService.addWord(name);
        Info plantInfo = infoService.getPlantInfo(name);
        InfoResponse.getPlantInfo returnPlantInfo = new InfoResponse.getPlantInfo(plantInfo);
        LocalDate now = LocalDate.now();
        int monthValue = now.getMonthValue();
        String waterType = "";
        returnPlantInfo.setMonthValue(monthValue);

        switch(monthValue){
            case 1: case 2: case 12:
                waterType = plantInfo.getWater_winter();
                break;
            case 3: case 4: case 5:
                waterType = plantInfo.getWater_spring();
                break;
            case 6: case 7: case 8:
                waterType = plantInfo.getWater_summer();
                break;
            case 9: case 10: case 11:
                waterType = plantInfo.getWater_autumn();
                break;
        }
        returnPlantInfo.setWaterType(waterType);
        return ResponseEntity.ok(new JsonResponse(true, 200, "addPlantInfo", returnPlantInfo));
    }

    @PostMapping("info/addPlantInfo")
    public ResponseEntity<JsonResponse> addPlantInfo(@RequestPart("info") Info info, @RequestPart("image") MultipartFile file){

        Info plantInfo = infoService.addPlantInfo(info, file);
        return ResponseEntity.ok(new JsonResponse(true, 200, "addPlantInfo", plantInfo));
    }

    @PostMapping("info/getRecommendInfo")
    public ResponseEntity<JsonResponse> getRecommendInfo(@RequestParam String name) {
        List<InfoResponse.getSearchList> searchInfo = infoService.recommendInfo(name);
        return ResponseEntity.ok(new JsonResponse(true, 200, "recommendInfo", searchInfo));
    }
}
