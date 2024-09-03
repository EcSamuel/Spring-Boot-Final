package com.rulezero.playerconnector.dao;

import com.rulezero.playerconnector.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersDao extends JpaRepository<Users, Long> {
    List<Users> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String query, String query1);

    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.availabilities LEFT JOIN FETCH u.userGames LEFT JOIN FETCH u.userStores WHERE u.userId = :userId")
    Optional<Users> findByIdWithAssociations(@Param("userId") Long userId);

    @Query("SELECT DISTINCT u FROM Users u LEFT JOIN FETCH u.availabilities LEFT JOIN FETCH u.userGames LEFT JOIN FETCH u.userStores")
    List<Users> findAllWithAssociations();
}
