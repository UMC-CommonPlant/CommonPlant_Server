package com.commonplant.umc.service;

import com.commonplant.umc.config.exception.BadRequestException;
import com.commonplant.umc.config.exception.ErrorResponseStatus;
import com.commonplant.umc.domain.Memo;
import com.commonplant.umc.domain.Place;
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
    private final PlaceService placeService;
    private final PlantService plantService;

    @Transactional
    public Long addMemo(User user, Long plant, String content, MultipartFile file){

        // TODO: 장소에 속해 있는 유저만 메모를 추가 가능하게 하기
        Place place = plantService.getPlant(plant).getPlace();

        if (!placeService.ExistUserInPlace(user, place)) {
            throw new BadRequestException(ErrorResponseStatus.NOT_FOUND_USER_IN_PLACE);
        }

        // imgUrl Setter
        String newCode = null;
        Long plantMemoIdx = plant;

        String imgUrl = null;

        if(file.getSize()>0){
            newCode = randomCode();
            imgUrl = firebaseService.uploadFiles("commonPlant_memo_" + plantMemoIdx + "_" + newCode, file);
        }

        Plant memoPlant = plantService.getPlant(plantMemoIdx);

        System.out.println(plantMemoIdx);

        // TODO: 식물의 애칭이 등록되어 있지 않거나 10자를 넘어갈 경우 예외처리
        String memoContent = null;

        if (content.length() == 0) {
            throw new BadRequestException(ErrorResponseStatus.NO_MEMO_CONTENT);
        } else if (content.length() <= 200) {
            memoContent = content;
        } else {
            throw new BadRequestException(ErrorResponseStatus.LONG_MEMO_CONTENT);
        }

        Memo memo = Memo.builder()
                .plant(memoPlant)
                .writer(user)
                .content(memoContent)
                .imgUrl(imgUrl)
                .build();

        memoRepository.save(memo);

//        String memoTest = " 메모 작성자의 닉네임: " + memo.getWriter().getNickName()
//                + " 메모 작성자의 프로필 사진: " + memo.getWriter().getUserImgUrl()
//                + " 메모에 업로드된 식물의 사진: " + memo.getImgUrl()
//                + " 메모 내용: " + memo.getContent()
//                + " 메모 작성일: " + memo.getCreatedAt();
//
//        return memoTest;

        return memo.getPlant().getPlantIdx();
    }

    @Transactional
    public MemoResponse.memoCardRes getMemoCard(Long memoIdx){

        Memo memo = memoRepository.findByMemoIdx(memoIdx);

        // TODO: 메모 작성일 Parsing -> CreatedAt()을 memoCardRes에서 @JsonFormat을 사용하여 해결

        MemoResponse.memoCardRes testRes = new MemoResponse.memoCardRes(
                memo.getMemoIdx(),
                // memo.getPlant(),
                memo.getPlant().getPlantIdx(),
                // memo.getWriter(),
                memo.getWriter().getNickName(),
                memo.getWriter().getUserImgUrl(),
                memo.getContent(),
                memo.getImgUrl(),
                memo.getCreatedAt()
        );

        return testRes;
    }

    @Transactional
    public MemoResponse.memoListRes getMemoList(Long plantIdx, User user){

        // TODO: 장소에 속해 있는 유저만 메모 리스트를 조회 가능하게 하기
        Place place = plantService.getPlant(plantIdx).getPlace();

        if (!placeService.ExistUserInPlace(user, place)) {
            throw new BadRequestException(ErrorResponseStatus.NOT_FOUND_USER_IN_PLACE);
        }

        // Getter
        List<Memo> memoList = memoRepository.findAllByPlantIdxOrderByCreatedAtDesc(plantIdx);

        List<MemoResponse.memoCardRes> memoCardListDto = memoList.stream().
                map(memo -> new MemoResponse.memoCardRes(
                                memo.getMemoIdx(),
                                // memo.getPlant(),
                                memo.getPlant().getPlantIdx(),
                                // memo.getWriter(),
                                memo.getWriter().getNickName(),
                                memo.getWriter().getUserImgUrl(),
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
    public Long updateMemo(Long memoIdx, User user, String content, MultipartFile file){

        Memo memo = memoRepository.findByMemoIdx(memoIdx);

        // TODO: 메모의 작성한 사람과 수정하려는 사람이 같은지 비교
        User writer = memo.getWriter();

        if (writer != user) {
            throw new BadRequestException(ErrorResponseStatus.INVALID_USER_JWT);
        }

        // TODO: 식물의 애칭이 등록되어 있지 않거나 10자를 넘어갈 경우 예외처리
        String memoContent = null;

        if (content.length() == 0) {
            throw new BadRequestException(ErrorResponseStatus.NO_MEMO_CONTENT);
        } else if (content.length() <= 200) {
            memoContent = content;
        } else {
            throw new BadRequestException(ErrorResponseStatus.LONG_MEMO_CONTENT);
        }

        // imgUrl Setter
        // 이미지가 바뀌더라도 url은 똑같게!
        String imgUrl = null;
        String newCode = null;
        Long plantMemoIdx = memoIdx;

        // TODO: 4가지 경우 모두 고려하기
        if (memo.getImgUrl() == null) {
            if (file.getSize() > 0) {
                newCode = randomCode();
                imgUrl = firebaseService.uploadFiles("commonPlant_memo_" + plantMemoIdx + "_" + newCode, file);
            }
        } else {
            if (file.getSize() > 0) {
                imgUrl = memo.getImgUrl();
            }
        }

        memo.updateMemo(
                memoContent,
                imgUrl
        );

        memoRepository.save(memo);
//
//        String updateMemoTest = " 식물 이름: " + memo.getPlant() +
//                " 메모 수정자: " + memo.getWriter() + " 수정 내용: " + memo.getContent()
//                + " 수정된 이미지 url: " + memo.getImgUrl()
//                + " 메모가 수정된 날짜: " + memo.getCreatedAt();
//
//        System.out.println(updateMemoTest);

        return memo.getPlant().getPlantIdx();
    }

    @Transactional
    public Long deleteMemo(Long memoIdx, User user){

        Memo memo = memoRepository.findByMemoIdx(memoIdx);

        // TODO: 메모의 작성한 사람과 수정하려는 사람이 같은지 비교
        User writer = memo.getWriter();

        if (writer != user) {
            throw new BadRequestException(ErrorResponseStatus.INVALID_USER_JWT);
        }

        memoRepository.deleteById(memoIdx);

        return memoIdx;
    }

    public String randomCode(){
        return RandomStringUtils.random(6,33,125,true,false);
    }
}