package com.rulezero.playerconnector.dao;

import com.rulezero.playerconnector.entity.Stores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoresDao extends JpaRepository<Stores, Long> {
}
