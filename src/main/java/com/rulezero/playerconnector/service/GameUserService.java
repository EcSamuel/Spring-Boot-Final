package com.rulezero.playerconnector.service;

import com.rulezero.playerconnector.dao.GamesDao;
import com.rulezero.playerconnector.dao.UsersDao;
import com.rulezero.playerconnector.entity.Games;
import com.rulezero.playerconnector.entity.Users;
import com.rulezero.playerconnector.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;

@Service
public class GameUserService {
    private final UsersDao usersDao;
    private final GamesDao gamesDao;

    @Autowired
    public GameUserService(UsersDao usersDao, GamesDao gamesDao) {
        this.usersDao = usersDao;
        this.gamesDao = gamesDao;
    }

    @Transactional
    public void addUserToGame(Long userId, Long gameId) {
        Users user = usersDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Games game = gamesDao.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));

        user.getUserGames().add(game);
        game.getPlayers().add(user);

        usersDao.save(user);
        gamesDao.save(game);
    }

    @Transactional
    public void removeUserFromGame(Long userId, Long gameId) {
        Users user = usersDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Games game = gamesDao.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));

        user.getUserGames().remove(game);
        game.getPlayers().remove(user);

        usersDao.save(user);
        gamesDao.save(game);
    }

    @Transactional
    public void updateGamePlayers(Long gameId, Collection<Long> userIds) {
        Games game = gamesDao.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));

        Set<Users> newPlayers = new HashSet<>(usersDao.findAllById(userIds));

        game.getPlayers().clear();
        game.getPlayers().addAll(newPlayers);

        newPlayers.forEach(user -> user.getUserGames().add(game));

        gamesDao.save(game);
        usersDao.saveAll(newPlayers);
    }

    @Transactional
    public void updateUserGames(Long userId, Collection<Long> gameIds) {
        Users user = usersDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Set<Games> newGames = new HashSet<>(gamesDao.findAllById(gameIds));

        user.getUserGames().clear();
        user.getUserGames().addAll(newGames);

        newGames.forEach(game -> game.getPlayers().add(user));

        usersDao.save(user);
        gamesDao.saveAll(newGames);
    }
}
