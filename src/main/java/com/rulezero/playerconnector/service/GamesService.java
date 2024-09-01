package com.rulezero.playerconnector.service;

import com.rulezero.playerconnector.controller.model.GamesData;
import com.rulezero.playerconnector.dao.GamesDao;
import com.rulezero.playerconnector.entity.Games;
import com.rulezero.playerconnector.entity.Users;
import com.rulezero.playerconnector.entity.Stores;
import com.rulezero.playerconnector.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GamesService {

    @Autowired
    private GamesDao gamesDao;

    @Transactional
    public GamesData saveGame(GamesData gamesData) {
        Games game = convertToEntity(gamesData);
        Games savedGame = gamesDao.save(game);
        return convertToGamesData(savedGame);
    }

    @Transactional
    public GamesData patchGame(Long gameId, GamesData gamesData) throws ResourceNotFoundException {
        Games existingGame = gamesDao.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));

        updateGameFields(existingGame, gamesData);

        Games updatedGame = gamesDao.save(existingGame);
        return convertToGamesData(updatedGame);
    }
    // Tried to add transactional here and it didn't like that
    private void updateGameFields(Games game, GamesData gamesData) {
        if (gamesData.getGameName() != null) {
            game.setName(gamesData.getGameName());
        }
        if (gamesData.getMinPlayers() != null) {
            game.setMinPlayers(gamesData.getMinPlayers());
        }
        if (gamesData.getMaxPlayers() != null) {
            game.setMaxPlayers(gamesData.getMaxPlayers());
        }
        if (gamesData.getGameDescription() != null) {
            game.setDescription(gamesData.getGameDescription());
        }
        if (gamesData.getGameUsers() != null) {
            updatePlayers(game, gamesData.getGameUsers());
        }
        if (gamesData.getStores() != null) {
            updateStores(game, gamesData.getStores());
        }
    }

    private void updatePlayers(Games game, Set<Users> newPlayers) {
        // Remove players not in the new set
        game.getPlayers().removeIf(player -> !newPlayers.contains(player));

        // Add new players
        newPlayers.forEach(game::addPlayer);
    }

    private void updateStores(Games game, Set<Stores> newStores) {
        // Remove stores not in the new set
        game.getStores().removeIf(store -> !newStores.contains(store));

        // Add new stores
        newStores.forEach(game::addStore);
    }

    public List<GamesData> searchGamesByName(String query) {
        List<Games> games = gamesDao.findByNameContainingIgnoreCase(query);
        return games.stream()
                .map(this::convertToGamesData)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteGame(Long gameId) throws ResourceNotFoundException {
        Games game = gamesDao.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));

        // Remove the game from all players and stores
        game.getPlayers().forEach(player -> player.getUserGames().remove(game));
        game.getStores().forEach(store -> store.getStoreGames().remove(game));

        gamesDao.delete(game);
    }

    private Games convertToEntity(GamesData gamesData) {
        Games game = new Games();
        game.setName(gamesData.getGameName());
        game.setMinPlayers(gamesData.getMinPlayers());
        game.setMaxPlayers(gamesData.getMaxPlayers());
        game.setDescription(gamesData.getGameDescription());

        if (gamesData.getGameUsers() != null) {
            gamesData.getGameUsers().forEach(game::addPlayer);
        }

        if (gamesData.getStores() != null) {
            gamesData.getStores().forEach(game::addStore);
        }

        return game;
    }

    private GamesData convertToGamesData(Games game) {
        GamesData gamesData = new GamesData();
        gamesData.setGameId(game.getGameId());
        gamesData.setGameName(game.getName());
        gamesData.setMinPlayers(game.getMinPlayers());
        gamesData.setMaxPlayers(game.getMaxPlayers());
        gamesData.setGameDescription(game.getDescription());
        gamesData.setGameUsers(game.getPlayers());
        gamesData.setStores(game.getStores());
        return gamesData;
    }

    public GamesData getGameById(Long gameId) {
        Games game = gamesDao.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));

        return convertToGamesData(game);
    }

    @Transactional
    public GamesData updateGamePlayers(Long gameId, Set<Users> newPlayers) throws ResourceNotFoundException {
        Games game = gamesDao.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));
        updatePlayers(game, newPlayers);
        Games updatedGame = gamesDao.save(game);
        return convertToGamesData(updatedGame);
    }

    @Transactional
    public GamesData updateGameStores(Long gameId, Set<Stores> newStores) throws ResourceNotFoundException {
        Games game = gamesDao.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));
        updateStores(game, newStores);
        Games updatedGame = gamesDao.save(game);
        return convertToGamesData(updatedGame);
    }

    @Transactional
    public void deleteGames(List<Long> gameIds) {
        List<Games> games = gamesDao.findAllById(gameIds);
        games.forEach(game -> {
            game.getPlayers().forEach(player -> player.getUserGames().remove(game));
            game.getStores().forEach(store -> store.getStoreGames().remove(game));
            gamesDao.delete(game);
        });
    }

    public List<GamesData> getAllGames() {
        List<Games> games = gamesDao.findAll();
        return games.stream()
                .map(this::convertToGamesData)
                .collect(Collectors.toList());
    }

}