package com.commonplant.umc.service;

import com.commonplant.umc.domain.Memo;
import com.commonplant.umc.dto.memo.MemoRequest;
import com.commonplant.umc.dto.memo.MemoResponse;
import com.commonplant.umc.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional
    public String addMemo(MemoRequest.addMemo req){

        String imgUrl = " ";

        Memo memo = Memo.builder()
                .plant(req.getPlant())
                .user(req.getUser())
                .content(req.getContent())
                .build();

        memoRepository.save(memo);

        String memoTest = " 식물 이름: " + memo.getPlant() +
                " 메모 작성자: " + memo.getUser() + " 메모 내용: " + memo.getContent();

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
            memo.getImgUrl()
        );

        return testRes;
    }

    @Transactional
    public String updateMemo(Long memoIdx, MemoRequest.updateMemo req){

        Memo memo = memoRepository.findByMemoIdx(memoIdx);

        memo.updateMemo(
                req.getPlant(),
                req.getUser(),
                req.getContent(),
                req.getImgUrl()
        );

        String updateMemoTest = " 식물 이름: " + memo.getPlant() +
                " 메모 수정자: " + memo.getUser() + " 수정 내용: " + memo.getContent();

        return updateMemoTest;
    }

    @Transactional
    public Long deleteMemo(Long memoIdx){

        Memo memo = memoRepository.findByMemoIdx(memoIdx);

        memoRepository.deleteById(memoIdx);

        return memoIdx;
    }
}
