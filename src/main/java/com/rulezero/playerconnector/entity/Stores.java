package com.rulezero.playerconnector.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"storeGames", "storeUsers"})
@EqualsAndHashCode(exclude = {"storeGames", "storeUsers"})
@Table(name = "stores")
public class Stores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private Boolean storeDisabilityCheck;

    @Column(nullable = false, length = 128)
    private String storeCity;

    @Column(nullable = false, length = 128)
    private String storeRegion;

    @Column(nullable = false)
    private Boolean outsideFood;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "store_games",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Games> storeGames = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "store_users",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Users> storeUsers = new HashSet<>();

    @Column(nullable = false)
    private String storeAddress;

    @Column(nullable = false)
    private String storePhone;

    @Column
    private String storeEmail;

    @Column(nullable = false)
    private Integer storeParking;

    // Helper methods for managing relationships
    public void addGame(Games game) {
        this.storeGames.add(game);
        game.getStores().add(this);
    }

    public void removeGame(Games game) {
        this.storeGames.remove(game);
        game.getStores().remove(this);
    }

    public void addUser(Users user) {
        this.storeUsers.add(user);
        user.getUserStores().add(this);
    }

    public void removeUser(Users user) {
        this.storeUsers.remove(user);
        user.getUserStores().remove(this);
    }
}