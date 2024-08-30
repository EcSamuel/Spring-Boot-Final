package com.rulezero.playerconnector.dao;

import com.rulezero.playerconnector.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersDao extends JpaRepository<Users, Long> {
}
