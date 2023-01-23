package com.commonplant.umc.controller;


import com.commonplant.umc.domain.Info;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.service.InfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;

//    db연결 코드 추가 필요
//    @GetMapping("/info/searchInfo/")
//    public ResponseEntity<JsonResponse> searchInfo(@RequestParam String name) throws ExecutionException, InterruptedException {
//        ArrayList<String> searchInfo = infoService.searchInfo(name);
//        return ResponseEntity.ok(new JsonResponse(true, 200, "searchInfo", searchInfo));
//    }

    @GetMapping("/info/getPlantInfo/")
    public ResponseEntity<JsonResponse> getPlantInfo(@RequestParam String name) throws ExecutionException, InterruptedException {
        Info plantInfo = infoService.getPlantInfo(name);
        return ResponseEntity.ok(new JsonResponse(true, 200, "addPlantInfo", plantInfo));
    }

    @PostMapping("info/addPlantInfo")
    public ResponseEntity<JsonResponse> addPlantInfo(@RequestBody Info info){

        Info plantInfo = infoService.addPlantInfo(info);
        return ResponseEntity.ok(new JsonResponse(true, 200, "addPlantInfo", plantInfo));
    }

}
