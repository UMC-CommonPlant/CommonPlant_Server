package com.commonplant.umc.repository;

import com.commonplant.umc.domain.Belong;
import com.commonplant.umc.domain.Place;
import com.commonplant.umc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BelongRepository extends JpaRepository<Belong, Long> {

    @Query("SELECT b.place FROM Belong b WHERE b.user=?1 "  + "ORDER BY b.createdAt")
    List<Place> findAllByUserOrderByCreatedAtAsc(User user);

    @Query("SELECT b.user FROM Belong b WHERE b.place=?1 "  + "ORDER BY b.createdAt")
    List<User> findAllByPlaceOrderByCreatedAtAsc(Place place);


    public Optional<Belong> findByUserAndPlace(User user, Place place);

}