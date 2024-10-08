package com.rulezero.playerconnector.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Games {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    private String name;

    private Integer minPlayers;

    private Integer maxPlayers;

    private String description;

    @ManyToMany(mappedBy = "userGames")
    private Set<Users> players = new HashSet<>();

    // Helper methods for managing players
    public void addPlayer(Users user) {
        this.players.add(user);
        user.getUserGames().add(this);
    }

    public void removePlayer(Users user) {
        this.players.remove(user);
        user.getUserGames().remove(this);
    }
}

