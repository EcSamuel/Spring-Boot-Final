package com.rulezero.playerconnector.dao;

import com.rulezero.playerconnector.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersDao extends JpaRepository<Users, Long> {
    List<Users> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String query, String query1);
}
