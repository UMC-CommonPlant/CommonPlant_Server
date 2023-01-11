package com.commonplant.umc.service;


import com.commonplant.umc.controller.PlaceController;
import com.commonplant.umc.domain.Place;
import com.commonplant.umc.dto.place.PlaceRequest;
import com.commonplant.umc.dto.place.PlaceResponse;
import com.commonplant.umc.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;



    @Transactional    // 유저랑 이미지쪽 완성되면 그 때 인자 추가
    public String addPlace(PlaceRequest.addPlace req)
    {
        String imgUrl = null;

        Place place = Place.builder().name(req.getName()).adress(req.getAddress())
                .placeImgUrl(imgUrl).build();

        placeRepository.save(place);

        String test = "장소 명 : " +  place.getName() + "주소 : " + place.getAdress() +
                "이미지 url : " + place.getPlaceImgUrl();

        return test;

    }
}
