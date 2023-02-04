package com.commonplant.umc.service;


import com.commonplant.umc.repository.BelongRepository;
import com.commonplant.umc.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BelongService {

    private final PlaceRepository placeRepository;

    private final BelongRepository belongRepository;

    private final FirebaseService firebaseService;



}
