package com.rulezero.playerconnector.dao;

import com.rulezero.playerconnector.entity.Games;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamesDao extends JpaRepository<Games, Long> {
    List<Games> findByNameContainingIgnoreCase(String name);
}
