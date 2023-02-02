package com.commonplant.umc.service;

import com.commonplant.umc.domain.Memo;
import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.dto.memo.MemoRequest;
import com.commonplant.umc.dto.memo.MemoResponse;
import com.commonplant.umc.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final FirebaseService firebaseService;
    private final MemoRepository memoRepository;
    private final PlantService plantService;

    @Transactional
    public String addMemo(MemoRequest.addMemo req, MultipartFile file){

        String name = req.getUser();

        String imgUrl = null;

        if(file.getSize()>0){
            imgUrl = firebaseService.uploadFiles("commonPlant_" + name,file);
        }

        Plant plant = plantService.getPlant(req.getPlant());
        //System.out.println(plant.getPlantIdx());

        Memo memo = Memo.builder()
                .plant(plant)
                .user(req.getUser())
                .content(req.getContent())
                .imgUrl(imgUrl)
                .build();

        memoRepository.save(memo);

        String memoTest = " 식물 이름: " + memo.getPlant() +
                " 메모 작성자: " + memo.getUser() + " 메모 내용: " + memo.getContent() +
                " 메모 작성일: " + memo.getCreatedAt();

        return memoTest;
    }

    @Transactional
    public MemoResponse.memoCardRes getMemoCard(Long memoIdx){

        Memo memo = memoRepository.findByMemoIdx(memoIdx);

        MemoResponse.memoCardRes testRes = new MemoResponse.memoCardRes(
                memo.getMemoIdx(),
                memo.getPlant(),
                memo.getUser(),
                memo.getContent(),
                memo.getImgUrl(),
                memo.getCreatedAt()
        );

        return testRes;
    }

    @Transactional
    public MemoResponse.memoListRes getMemoList(Long plantIdx){

        // Getter
        List<Memo> memoList = memoRepository.findAllByPlantIdxOrderByCreatedAtDesc(plantIdx);

        List<MemoResponse.memoCardRes> memoCardListDto = memoList.stream().
                map(memo -> new MemoResponse.memoCardRes(
                                memo.getMemoIdx(),
                                memo.getPlant(),
                                memo.getUser(),
                                memo.getContent(),
                                memo.getImgUrl(),
                                memo.getCreatedAt()
                        )
                ).collect(Collectors.toList());

        // Setter
        List<List> plantAllMemoList = new ArrayList<>();
        List<MemoResponse.memoCardRes> memoListByCreatedAt = new ArrayList<>();

        // getCreatedAt()/getCreatedAt().toLocalDate()
        LocalDate creationDate = memoCardListDto.get(0).getCreatedAt().toLocalDate();

        for(int i = 0; i < memoCardListDto.size(); i++){
            memoListByCreatedAt = new ArrayList<>();
            memoListByCreatedAt.add(memoCardListDto.get(i));

            plantAllMemoList.add(memoListByCreatedAt);
        }

        MemoResponse.memoListRes memoListRes = new MemoResponse.memoListRes(plantAllMemoList);

        return memoListRes;
    }

    @Transactional
    public String updateMemo(Long memoIdx, MemoRequest.updateMemo req, MultipartFile file){

        String name = req.getUser();

        String imgUrl = null;

        if(file.getSize()>0){
            imgUrl = firebaseService.uploadFiles("commonPlant_" + name,file);
        }

        Plant plant = plantService.getPlant(req.getPlant());
        //System.out.println(plant.getPlantIdx());

        Memo memo = memoRepository.findByMemoIdx(memoIdx);

        memo.updateMemo(
                plant,
                req.getUser(),
                req.getContent(),
                imgUrl
        );

        String updateMemoTest = " 식물 이름: " + memo.getPlant() +
                " 메모 수정자: " + memo.getUser() + " 수정 내용: " + memo.getContent()
                + " 수정된 이미지 url: " + memo.getImgUrl()
                + " 메모가 수정된 날짜: " + memo.getCreatedAt();

        System.out.println(updateMemoTest);

        return updateMemoTest;
    }

    @Transactional
    public Long deleteMemo(Long memoIdx){

        Memo memo = memoRepository.findByMemoIdx(memoIdx);

        memoRepository.deleteById(memoIdx);

        return memoIdx;
    }
}