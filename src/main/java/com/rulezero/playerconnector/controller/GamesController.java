package com.rulezero.playerconnector.controller;


import com.rulezero.playerconnector.controller.model.GamesData;
import com.rulezero.playerconnector.exception.ResourceNotFoundException;
import com.rulezero.playerconnector.service.GamesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
@Slf4j
public class GamesController {
    @Autowired
    private GamesService gamesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GamesData createGame(@RequestBody GamesData gamesData) {
        log.info("Requesting Store Creation: {}", gamesData);
        return gamesService.saveGame(gamesData);
    }

    @PatchMapping("/{gameId}")
    public ResponseEntity<GamesData> partiallyUpdateGame(@PathVariable Long gameId, @RequestBody GamesData gamesData) {
        try {
            GamesData updatedGame = gamesService.patchGame(gameId, gamesData);
            return ResponseEntity.ok(updatedGame);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GamesData> getGameById(@PathVariable Long gameId) {
        try {
            GamesData gameData = gamesService.getGameById(gameId);
            return ResponseEntity.ok(gameData);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/search")
    public List<GamesData> searchGames(@RequestParam String query) {
        log.info("Requesting Game Search: {}", query);
        if (query == null || query.isEmpty()) {
            log.info("You must specify a query");
            return List.of(); // Return an empty list instead of null
        } else {
            return gamesService.searchGamesByName(query);
        }
    }

    @DeleteMapping("/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteGame(@PathVariable Long gameId) {
        log.info("Requesting Game Deletion: {}", gameId);
        try {
            gamesService.deleteGame(gameId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
