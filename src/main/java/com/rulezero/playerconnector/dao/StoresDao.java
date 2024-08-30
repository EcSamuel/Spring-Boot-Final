package com.rulezero.playerconnector.dao;

import com.rulezero.playerconnector.entity.Stores;
import com.rulezero.playerconnector.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoresDao extends JpaRepository<Stores, Long> {
    List<Stores> findByStoreNameContainingIgnoreCase(String query);
}
