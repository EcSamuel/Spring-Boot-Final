package com.rulezero.playerconnector.service;

import com.rulezero.playerconnector.controller.model.GamesData;
import com.rulezero.playerconnector.dao.GamesDao;
import com.rulezero.playerconnector.dao.UsersDao;
import com.rulezero.playerconnector.entity.Games;
import com.rulezero.playerconnector.entity.Users;
import com.rulezero.playerconnector.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GamesService {
    private final GamesDao gamesDao;

    @Autowired
    public GamesService(UsersDao usersDao, GamesDao gamesDao) {
        this.gamesDao = gamesDao;
    }

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
        // TODO: Add the relationship modifier to patch once it is fixed everywhere else.
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

        // Remove the game from all players and stores- TODO: This might not work anymore due to the refactor
        game.getPlayers().forEach(player -> player.getUserGames().remove(game));

        gamesDao.delete(game);
    }

    private Games convertToEntity(GamesData gamesData) {
        Games game = new Games();
        game.setName(gamesData.getGameName());
        game.setMinPlayers(gamesData.getMinPlayers());
        game.setMaxPlayers(gamesData.getMaxPlayers());
        game.setDescription(gamesData.getGameDescription());
        return game;
    }

    private GamesData convertToGamesData(Games game) {
        GamesData gamesData = new GamesData();
        gamesData.setGameId(game.getGameId());
        gamesData.setGameName(game.getName());
        gamesData.setMinPlayers(game.getMinPlayers());
        gamesData.setMaxPlayers(game.getMaxPlayers());
        gamesData.setGameDescription(game.getDescription());
//        gamesData.setGameUsers(game.getPlayers());
        return gamesData;
    }

    public GamesData getGameById(Long gameId) {
        Games game = gamesDao.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));

        return convertToGamesData(game);
    }

    @Transactional
    public void deleteGames(List<Long> gameIds) {
        List<Games> games = gamesDao.findAllById(gameIds);
        games.forEach(game -> {
            game.getPlayers().forEach(player -> player.getUserGames().remove(game));
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