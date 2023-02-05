package com.commonplant.umc.repository;

import com.commonplant.umc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //  List<User> findByNameContains(String name);
//
//    Boolean existsByName(String name);
//
//    List<User> findByNameAndPassword(String name, String password);

    @Query("select u from User u where u.email=?1 and u.platform=?2")
    User findUserByEmail(String email, String loginType);


    @Query("select count(u) from User u where u.email=?1 and u.platform=?2")
    int countUserByEmail(String email, String loginType);

    Optional<User> findUserByUuid(String uuid);
}
