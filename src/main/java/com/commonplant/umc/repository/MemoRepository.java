package com.commonplant.umc.repository;

import com.commonplant.umc.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> { // 상속 후 JPA 메서드 사용

    // SELECT * FROM memo WHERE memo.plant_idx = (메모 리스트를 보고자하는 식물의 plant_idx) ORDER BY memo.created_at DESC;
    @Query(value = "SELECT m FROM Memo m WHERE m.plant.plantIdx = ?1 "
            + "ORDER BY m.createdAt DESC")
    List<Memo> findAllByPlantIdxOrderByCreatedAtDesc(Long plantIdx);

    // findByMemoIdx(): 메모 수정/삭제할 때 사용
    Memo findByMemoIdx(Long memoIdx);
}