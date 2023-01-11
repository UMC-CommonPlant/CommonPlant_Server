package com.commonplant.umc.controller;


import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.place.PlaceRequest;
import com.commonplant.umc.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping("/place/add")
    public ResponseEntity<JsonResponse> addPlace(@RequestBody PlaceRequest.addPlace req){

        String test = placeService.addPlace(req);

        return ResponseEntity.ok(new JsonResponse(true, 200,"addPlace", test));
    }


}
