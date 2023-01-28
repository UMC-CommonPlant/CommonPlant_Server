package com.commonplant.umc.repository;

import com.commonplant.umc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameContains(String name);

    Boolean existsByName(String name);

    List<User> findByNameAndPassword(String name, String password);
}
