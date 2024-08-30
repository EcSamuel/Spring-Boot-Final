package com.rulezero.playerconnector.entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Stores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    // other fields...

    @ManyToMany(mappedBy = "stores")
    private Set<Games> games = new HashSet<>();

    public Collection<Object> getGames() {
        return null;
    }

    // getters and setters...
}
