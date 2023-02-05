package com.commonplant.umc.service;

import com.commonplant.umc.domain.Memo;
import com.commonplant.umc.domain.Plant;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.memo.MemoRequest;
import com.commonplant.umc.dto.memo.MemoResponse;
import com.commonplant.umc.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
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

    private final MemoRepository memoRepository;

    private final FirebaseService firebaseService;
    private final PlantService plantService;

    @Transactional
    public String addMemo(User user, MemoRequest.addMemo req, MultipartFile file){

        // imgUrl Setter
        String newCode = randomCode();
        Long plantMemoIdx = req.getPlant();

        String imgUrl = null;

        if(file.getSize()>0){
            imgUrl = firebaseService.uploadFiles("commonPlant_memo/" +
                    plantMemoIdx + "_" + newCode,file);
        }

        // User user = userService.getUser(req.getWriter());
        Plant plant = plantService.getPlant(req.getPlant());
        // System.out.println(plant.getPlantIdx());

        System.out.println("plantMemoIdx");

        Memo memo = Memo.builder()
                .plant(plant)
                .writer(user)
                .content(req.getContent())
                .imgUrl(imgUrl)
                .build();

        memoRepository.save(memo);

        String memoTest = " 식물 애칭: " + memo.getPlant().getNickname() +
                " 메모 작성자: " + memo.getWriter() + " 메모 내용: " + memo.getContent() +
                " 메모 작성일: " + memo.getCreatedAt() + " 메모를 추가한 식물의 인덱스는: " + memo.getPlant().getPlantIdx();

        return memoTest;
    }

    @Transactional
    public MemoResponse.memoCardRes getMemoCard(Long memoIdx){

        Memo memo = memoRepository.findByMemoIdx(memoIdx);

        // TODO: 메모 작성일 Parsing -> CreatedAt()을 memoCardRes에서 @JsonFormat을 사용하여 해결

        MemoResponse.memoCardRes testRes = new MemoResponse.memoCardRes(
                memo.getMemoIdx(),
                memo.getPlant(),
                memo.getWriter(),
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
                                memo.getWriter(),
                                memo.getContent(),
                                memo.getImgUrl(),
                                memo.getCreatedAt()
                        )
                ).collect(Collectors.toList());

        // Setter
        List<List> plantAllMemoList = new ArrayList<>();
        List<MemoResponse.memoCardRes> memoListByCreatedAt = new ArrayList<>();

        // TODO: 식물에 등록된 메모가 없을 경우에 대한 예외처리
        // getCreatedAt()/getCreatedAt().toLocalDate()
        if(memoCardListDto.size() != 0) {
            LocalDate creationDate = memoCardListDto.get(0).getCreatedAt().toLocalDate();
        }

        for(int i = 0; i < memoCardListDto.size(); i++){
            memoListByCreatedAt = new ArrayList<>();
            memoListByCreatedAt.add(memoCardListDto.get(i));

            plantAllMemoList.add(memoListByCreatedAt);
        }

        MemoResponse.memoListRes memoListRes = new MemoResponse.memoListRes(plantAllMemoList);

        return memoListRes;
    }

    @Transactional
    public String updateMemo(Long memoIdx, User user, MemoRequest.updateMemo req, MultipartFile file){

        // imgUrl Setter
        String newCode = randomCode();
        Long plantMemoIdx = req.getPlant();

        String imgUrl = null;

        if(file.getSize()>0){
            imgUrl = firebaseService.uploadFiles("commonPlant_memo/" +
                    plantMemoIdx + "_" + newCode,file);
        }

        Plant plant = plantService.getPlant(req.getPlant());
        //System.out.println(plant.getPlantIdx());

        Memo memo = memoRepository.findByMemoIdx(memoIdx);

        memo.updateMemo(
                plant,
                user,
                req.getContent(),
                imgUrl
        );

        memoRepository.save(memo);

        String updateMemoTest = " 식물 이름: " + memo.getPlant() +
                " 메모 수정자: " + memo.getWriter() + " 수정 내용: " + memo.getContent()
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

    public String randomCode(){
        return RandomStringUtils.random(6,33,125,true,false);
    }
}