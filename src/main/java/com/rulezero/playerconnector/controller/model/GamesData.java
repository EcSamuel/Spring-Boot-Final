package com.rulezero.playerconnector.controller.model;

import lombok.Data;

import java.util.Set;

@Data
public class GamesData {
    private Long gameId;
    private String gameName;
    private String gameDescription;
    private Set<String> players; // unsure about this usage type
    private Integer minPlayers;
    private Integer maxPlayers;
}
