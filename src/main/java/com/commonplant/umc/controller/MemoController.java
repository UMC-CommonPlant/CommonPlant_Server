package com.commonplant.umc.controller;

import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.memo.MemoRequest;
import com.commonplant.umc.dto.memo.MemoResponse;
import com.commonplant.umc.dto.plant.PlantResponse;
import com.commonplant.umc.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    // 메모 등록 (POST)
    @PostMapping("/memo/add")
    public ResponseEntity<JsonResponse> addMemo(@RequestPart("memo") MemoRequest.addMemo req, @RequestPart("image") MultipartFile file){

        System.out.println("=============ADD MEMO TEST.NAME===============" + req.getUser() + req.getContent());
        System.out.println("=============ADD MEMO TEST.NAME===============" + file);
        // System.out.println("=============ADD MEMO TEST.NAME===============" + req.getCreatedAt());

        String memoTest = memoService.addMemo(req, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "addMemo", memoTest));
    }

    // 특정 메모 조회 (GET)
    @GetMapping("/memo/card/{memoIdx}")
    public ResponseEntity<JsonResponse> getMemoCard(@PathVariable Long memoIdx) {

        System.out.println("=============GET MEMO TEST.NAME===============");

        MemoResponse.memoCardRes res = memoService.getMemoCard(memoIdx);

        return ResponseEntity.ok(new JsonResponse(true,200, "getMemoCard", res));
    }

    // 메모 리스트 조회 (GET)
    // 식물 페이지/캘린더 뷰: 식물 페이지 기준으로 구성!
    // 식물 별로 조회해야 하는데... plant 테이블의 plant_idx 사용?
    @GetMapping("/plant/{plantIdx}/memoList")
    public ResponseEntity<JsonResponse> getMemoList(@PathVariable Long plantIdx){

        System.out.println("=============GET MEMO LIST TEST.NAME===============");

        MemoResponse.memoListRes res = memoService.getMemoList(plantIdx);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getMemoList", res));
    }

    // 메모 수정 (PATCH: plant랑 user는 업데이트 되지 않음)
    @PutMapping("/memo/update/{memoIdx}")
    public ResponseEntity<JsonResponse> updateMemo(@PathVariable Long memoIdx,
                                                   @RequestPart("memo") MemoRequest.updateMemo req,
                                                   @RequestPart("image") MultipartFile file){

        System.out.println("=============UPDATE MEMO TEST.NAME==============" + req.getContent());
        System.out.println("=============UPDATE MEMO TEST.NAME==============" + file);
        // System.out.println("=============UPDATE PLANT TEST.NAME===============" + req.getCreatedAt());

        String updateMemoTest = memoService.updateMemo(memoIdx, req, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updateMemo", updateMemoTest));
    }

    // 메모 삭제 (선택한 메모를 삭제)
    @DeleteMapping("/memo/delete/{memoIdx}")
    public ResponseEntity deleteMemo(@PathVariable Long memoIdx){

        System.out.println("=============DELETE MEMO TEST.NAME==============");

        Long deletedMemoIdx = memoService.deleteMemo(memoIdx);

        return ResponseEntity.ok(new JsonResponse(true, 200, "deleteMemo", deletedMemoIdx));
    }
}