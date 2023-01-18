package com.commonplant.umc.service;


import com.commonplant.umc.config.exception.BadRequestException;
import com.commonplant.umc.domain.Belong;
import com.commonplant.umc.domain.Place;
import com.commonplant.umc.dto.place.PlaceRequest;
import com.commonplant.umc.repository.BelongRepository;
import com.commonplant.umc.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;

import static com.commonplant.umc.config.exception.ErrorResponseStatus.PLACE_CODE_ERROR;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    private final BelongRepository belongRepository;



    @Transactional    // 유저랑 이미지쪽 완성되면 그 때 인자 추가
    public String addPlace(User user, PlaceRequest.addPlace req)
    {
        String newCode = randomCode();

        String imgUrl = "img url test";

        Place place = Place.builder().name(req.getName()).address(req.getAddress())
                .placeImgUrl(imgUrl).code(newCode).build();
        placeRepository.save(place);

        Belong belong = Belong.builder().user(user).place(place).build();
        belongRepository.save(belong);

        return place.getCode();
    }

    public String randomCode(){
        return RandomStringUtils.random(6,33,125,true,false);
    }

    public Place getPlace(String placeCode) {
        Place place = placeRepository.findByCode(placeCode).orElseThrow(()->new BadRequestException(PLACE_CODE_ERROR));

        return place;
    }
}
