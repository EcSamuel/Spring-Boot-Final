package com.rulezero.playerconnector.handler;

import com.rulezero.playerconnector.controller.model.GamesData;
import com.rulezero.playerconnector.service.GamesService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Scanner;

public class GameMenuHandler {

    @Autowired
    private GamesService gamesService;

    private Scanner scanner = new Scanner(System.in);

    public void process() {
        List<String> gameMenu = List.of(
                "1) Add a Game",
                "2) List Games",
                "3) Select a Game",
                "4) Update a Game",
                "5) Delete a Game",
                "0) Back"
        );

        boolean back = false;
        while (!back) {
            int selection = getUserSelection(gameMenu);
            switch (selection) {
                case 1 -> addGame();
                case 2 -> listGames();
                case 3 -> selectGame();
                case 4 -> updateGame();
                case 5 -> deleteGame();
                case 0 -> back = true;
                default -> System.out.println("Invalid selection");
            }
        }
    }

    private int getUserSelection(List<String> menu) {
        System.out.println("\nMake a selection:");
        menu.forEach(System.out::println);
        System.out.println("You selected ");
        return Integer.parseInt(scanner.nextLine());
    }

    private void addGame() {
        System.out.println("Enter game name:");
        String gameName = scanner.nextLine();

        System.out.println("Enter game description");
        String gameDescription = scanner.nextLine();

        System.out.println("Enter Minimum number of Players");
        int minPlayers = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Maximum players");
        int maxPlayers = Integer.parseInt(scanner.nextLine());

        GamesData newGame = new GamesData();
        newGame.setGameName(gameName);
        newGame.setGameDescription(gameDescription);
        newGame.setMinPlayers(minPlayers);
        newGame.setMaxPlayers(maxPlayers);
        newGame.setGameUsers(null);
        newGame.setStores(null);

        GamesData savedGame =gamesService.saveGame(newGame);
        System.out.println("Game attempting add" +savedGame);
    }

    private void listGames() {
        List<GamesData> games = gamesService.getAllGames();
        games.forEach(game -> System.out.println(game.toString()));
    }

    private void updateGame() {
        System.out.println("Enter game ID to update:");
        Long gameId = Long.parseLong(scanner.nextLine());

        System.out.println("Enter new game name:");
        String name = scanner.nextLine();

        System.out.println("Enter new game genre:");
        String genre = scanner.nextLine();

        System.out.println("Enter new release date (YYYY-MM-DD):");
        String releaseDate = scanner.nextLine();

        // Fetch the existing game, update it, and save it
        GamesData existingGame = gamesService.getGameById(gameId);
        existingGame.setGameName(name);
        existingGame.setGameDescription(gameDescription);
        existingGame.setMinPlayers(minPlayers);
        existingGame.setMaxPlayers(maxPlayers);
        existingGame.setGameUsers(null);
        existingGame.setStores(null);

        GamesData updatedGame = gamesService.updateGame(existingGame);
        System.out.println("Game updated: " + updatedGame);
    }

    private void selectGame() {
        System.out.println("Enter game ID:");
        Long gameId = Long.parseLong(scanner.nextLine());

        GamesData game = gamesService.getGameById(gameId);
        System.out.println("Selected game: " + game);
    }

    private void deleteGame() {
        System.out.println("Enter game ID to delete:");
        Long gameId = Long.parseLong(scanner.nextLine());

        gamesService.deleteGame(gameId);
        System.out.println("Game deleted.");
    }
}
