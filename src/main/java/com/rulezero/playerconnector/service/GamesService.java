package com.rulezero.playerconnector.service;

import com.rulezero.playerconnector.controller.model.GamesData;
import com.rulezero.playerconnector.dao.GamesDao;
import com.rulezero.playerconnector.entity.Games;
import com.rulezero.playerconnector.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GamesService {

    @Autowired
    private GamesDao gamesDao;

    // Save Game method
    public GamesData saveGame(GamesData gamesData) {
        // Convert GamesData to Games entity
        Games game = convertToEntity(gamesData);

        // Save the game entity
        Games savedGame = gamesDao.save(game);

        // Convert back to GamesData
        return convertToGamesData(savedGame);
    }

    // Patch Game method
    public GamesData patchGame(Long gameId, GamesData gamesData) throws ResourceNotFoundException {
        Games existingGame = gamesDao.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));

        // Update fields that are not null in gamesData
        if (gamesData.getGameName() != null) {
            existingGame.setName(gamesData.getGameName());
        }
        if (gamesData.getMinPlayers() != null) {
            existingGame.setMinPlayers(gamesData.getMinPlayers());
        }
        if (gamesData.getMaxPlayers() != null) {
            existingGame.setMaxPlayers(gamesData.getMaxPlayers());
        }
        if (gamesData.getGameDescription() != null) {
            existingGame.setDescription(gamesData.getGameDescription());
        }
        // Handle players update if necessary

        // Save updated game
        Games updatedGame = gamesDao.save(existingGame);

        return convertToGamesData(updatedGame);
    }

    private Games convertToEntity(GamesData gamesData) {
        Games game = new Games();
        game.setName(gamesData.getGameName());
        game.setMinPlayers(gamesData.getMinPlayers());
        game.setMaxPlayers(gamesData.getMaxPlayers());
        game.setDescription(gamesData.getGameDescription());
        // Handle conversion of players if needed
        return game;
    }

    private GamesData convertToGamesData(Games game) {
        GamesData gamesData = new GamesData();
        gamesData.setGameId(game.getGameId());
        gamesData.setGameName(game.getName());
        gamesData.setMinPlayers(game.getMinPlayers());
        gamesData.setMaxPlayers(game.getMaxPlayers());
        gamesData.setGameDescription(game.getDescription());
        // Convert players if needed
        return gamesData;
    }

    public List<GamesData> searchGamesByName(String query) {
        List<Games> games = gamesDao.findGameNameContaining(query);
        return games.stream()
                .map(this::convertToGamesData)
                .collect(Collectors.toList());

    }

    public void deleteGame(Long gameId) {
        gamesDao.deleteById(gameId);
    }
}
