package com.commonplant.umc.repository;

import com.commonplant.umc.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> { // 상속 후 JPA 메서드 사용

    // List<Memo> findAllByOrderByModifiedAtDesc();

    // findByMemoIdx(): 메모 수정/삭제할 때 사용
    Memo findByMemoIdx(Long memoIdx);
}
