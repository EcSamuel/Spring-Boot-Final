package com.rulezero.playerconnector.controller.model;

import com.rulezero.playerconnector.entity.Users;
import com.rulezero.playerconnector.entity.Stores;
import lombok.Data;

import java.util.Set;

@Data
public class GamesData {
    private Long gameId;
    private String gameName;
    private String gameDescription;
    private Set<Users> gameUsers;
    private Set<Stores> stores;
    private Integer minPlayers;
    private Integer maxPlayers;
}