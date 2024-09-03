package com.rulezero.playerconnector.controller;

import com.rulezero.playerconnector.service.GameUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/game-users")
public class GameUserController {

    @Autowired
    private GameUserService gameUserService;

    @PostMapping("/{gameId}/users/{userId}")
    public ResponseEntity<Void> addUserToGame(@PathVariable Long gameId, @PathVariable Long userId) {
        gameUserService.addUserToGame(userId, gameId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{gameId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromGame(@PathVariable Long gameId, @PathVariable Long userId) {
        gameUserService.removeUserFromGame(userId, gameId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{gameId}/users")
    public ResponseEntity<Void> updateGamePlayers(@PathVariable Long gameId, @RequestBody Set<Long> userIds) {
        gameUserService.updateGamePlayers(gameId, userIds);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{userId}/games")
    public ResponseEntity<Void> updateUserGames(@PathVariable Long userId, @RequestBody Set<Long> gameIds) {
        gameUserService.updateUserGames(userId, gameIds);
        return ResponseEntity.ok().build();
    }
}
