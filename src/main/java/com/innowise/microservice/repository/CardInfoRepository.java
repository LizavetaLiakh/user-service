package com.innowise.microservice.repository;

import com.innowise.microservice.entity.CardInfo;
import com.innowise.microservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {

    Optional<CardInfo> findByNumber(String number);

    @Modifying
    @Transactional
    @Query(value = "UPDATE card_info SET user_id = :userId, number = :number, holder = :holder, expiration_date = " +
            ":expirationDate WHERE id = :id", nativeQuery = true)
    int updateCardInfo(@Param("id") Long id, @Param("userId") Long userId, @Param("number") String number,
                       @Param("holder") String holder, @Param("expirationDate") LocalDate expirationDate);
}
