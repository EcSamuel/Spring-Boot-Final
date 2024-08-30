package com.rulezero.playerconnector.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Users {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // Maps to user_id in your SQL

    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 128)
    private String lastName;

    @Column()
    private String userPhone;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String userAddress;

    @Column(nullable = false, length = 128)
    private String userCity;

    @Column(nullable = false, length = 128)
    private String userRegion;

    @Column(nullable = false, unique = true)
    private String userLoginName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "availability_id", nullable = false)
    private Availability userAvailability;

    @ManyToMany(mappedBy = "players")
    private Set<Games> userGames = new HashSet<>();

    @ManyToMany(mappedBy = "stores")
    private Set<Stores> userStores = new HashSet<>();

}