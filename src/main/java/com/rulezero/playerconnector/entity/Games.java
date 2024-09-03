package com.rulezero.playerconnector.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
public class Games {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    private String name;

    private Integer minPlayers;

    private Integer maxPlayers;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_games",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Users> players = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "store_games",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    private Set<Stores> stores = new HashSet<>();

    // Helper methods for managing players
    public void addPlayer(Users user) {
        this.players.add(user);
        user.getUserGames().add(this);
    }

    public void removePlayer(Users user) {
        this.players.remove(user);
        user.getUserGames().remove(this);
    }

    public void addStore(Stores stores) {
    }
}

