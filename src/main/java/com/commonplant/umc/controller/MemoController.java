package com.commonplant.umc.controller;

import com.commonplant.umc.config.jwt.JwtService;
import com.commonplant.umc.domain.User;
import com.commonplant.umc.dto.JsonResponse;
import com.commonplant.umc.dto.memo.MemoRequest;
import com.commonplant.umc.dto.memo.MemoResponse;
import com.commonplant.umc.service.MemoService;
import com.commonplant.umc.service.UserService;
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

    private final UserService userService;
    private final MemoService memoService;
    private final JwtService jwtService;

    // 메모 등록 (POST)
    // TODO: @RequestPart to @RequestParam
    @PostMapping("/memo/add")
    public ResponseEntity<JsonResponse> addMemo(@RequestParam("plant") Long plant,
                                                @RequestParam("content") String content,
                                                @RequestPart("image") MultipartFile file){

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);
        System.out.println(user.getEmail());

        System.out.println("=============ADD MEMO TEST.NAME===============" + plant);
        System.out.println("=============ADD MEMO TEST.NAME===============" + content);
        System.out.println("=============ADD MEMO TEST.NAME===============" + file);

        Long memoTest = memoService.addMemo(user, plant, content, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "addMemo", memoTest));
    }

    // 특정 메모 조회 (GET)
    @GetMapping("/memo/card/{memoIdx}")
    public ResponseEntity<JsonResponse> getMemoCard(@PathVariable Long memoIdx) {

        String uuid = jwtService.resolveToken();

        System.out.println("=============GET MEMO TEST.NAME===============");

        MemoResponse.memoCardRes res = memoService.getMemoCard(memoIdx);

        return ResponseEntity.ok(new JsonResponse(true,200, "getMemoCard", res));
    }

    // 메모 리스트 조회 (GET)
    // 식물 페이지/캘린더 뷰: 식물 페이지 기준으로 구성!
    // 식물 별로 조회해야 하는데... plant 테이블의 plant_idx 사용?
    @GetMapping("/plant/{plantIdx}/memoList")
    public ResponseEntity<JsonResponse> getMemoList(@PathVariable Long plantIdx){

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);
        System.out.println(user.getEmail());

        System.out.println("=============GET MEMO LIST TEST.NAME===============");

        MemoResponse.memoListRes res = memoService.getMemoList(plantIdx, user);

        return ResponseEntity.ok(new JsonResponse(true, 200, "getMemoList", res));
    }

    // 메모 수정 (PUT: plant는 업데이트 되지 않음)
    // TODO: @RequestPart to @RequestParam
    @PutMapping("/memo/update/{memoIdx}")
    public ResponseEntity<JsonResponse> updateMemo(@PathVariable Long memoIdx,
                                                   @RequestParam("content") String content,
                                                   @RequestPart("image") MultipartFile file){

        System.out.println("=============UPDATE MEMO TEST.NAME==============" + content);
        System.out.println("=============UPDATE MEMO TEST.NAME==============" + file);

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        Long updateMemoTest = memoService.updateMemo(memoIdx, user, content, file);

        return ResponseEntity.ok(new JsonResponse(true, 200, "updateMemo", updateMemoTest));
    }

    // 메모 삭제
    @DeleteMapping("/memo/delete/{memoIdx}")
    public ResponseEntity deleteMemo(@PathVariable Long memoIdx){

        System.out.println("=============DELETE MEMO TEST.NAME==============");

        String uuid = jwtService.resolveToken();
        User user = userService.getUser(uuid);

        Long deleteMemoTest = memoService.deleteMemo(memoIdx, user);

        return ResponseEntity.ok(new JsonResponse(true, 200, "deleteMemo", deleteMemoTest));
    }
}