package de.devofvictory.bwinf.exercise4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Exercise4 {

    private static boolean showDebug;
    private static int iterations;
    private static List<String> dices = new ArrayList<>();
    private static List<List<String>> combinations = new ArrayList<>();
    private static HashMap<Integer, Integer> scores = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        if (args.length == 3) {
            showDebug = Boolean.parseBoolean(args[0]);
            iterations = Integer.parseInt(args[1]);

            File diceFile = new File(args[2]);

            if (diceFile.exists()) {

                try {
                    FileReader fr = new FileReader(diceFile);
                    BufferedReader br = new BufferedReader(fr);

                    br.readLine();
                    String line = br.readLine();

                    while (line != null) {
                        dices.add(line);
                        line = br.readLine();
                    }

                    fr.close();
                    br.close();
                }catch (IOException ex) {
                    System.out.println("An error occurred while reading the dice file.");
                    ex.printStackTrace();
                    return;
                }

                for (int i=0; i<dices.size(); i++) {
                    scores.put(i, 0);
                    for (int j = i + 1; j < dices.size(); j++) {

                        List<String> combination = new ArrayList<>();
                        combination.add(dices.get(i));
                        combination.add(dices.get(j));

                        combinations.add(combination);
                    }
                }

            }else {
                System.out.println("The file seems not to exist.");
                return;
            }

        }else {
            System.out.println("Usage: java -jar Exercise4.java <boolean showDebug> <int iterations> <string pathToFile>");
            return;
        }

        HashMap<Integer, GamePlayer> houses = new HashMap<>();

        for (int a = 0; a<10; a++) {
            for (List<String> combination : combinations) {

                List<Integer> dice1 = Arrays.asList(combination.get(0).split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());
                List<Integer> dice2 = Arrays.asList(combination.get(1).split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());


                Game game = new Game(48, 0);

                GamePlayer player1 = new GamePlayer(game, "Player 1 (Green)", 0, 47, dice1.subList(1, dice1.size()));
                GamePlayer player2 = new GamePlayer(game, "Player 2 (Red)", 24, 23, dice2.subList(1, dice2.size()));

                houses.put(44, player1);
                houses.put(45, player1);
                houses.put(46, player1);
                houses.put(47, player1);

                houses.put(20, player2);
                houses.put(21, player2);
                houses.put(22, player2);
                houses.put(23, player2);

                game.getGamePlan().setHouses(houses);
                game.joinPlayer(player1);
                game.joinPlayer(player2);

                Exercise4.logMessage("========================================");
                Exercise4.logMessage("New game initialized.");
                Exercise4.logMessage("Dice 1: " + player1.getDice());
                Exercise4.logMessage("Dice 2: " + player2.getDice());
                Exercise4.logMessage("========================================");

                long startedTime = System.currentTimeMillis();
                while (game.isRunning()) {

                    if (System.currentTimeMillis() - startedTime >= 1500) {
                        logMessage("Simulation took too long. Skipped this game.");
                        break;
                    }

                    GamePlayer turnPlayer = game.getPlayers().get(game.getOnTurn());
                    turnPlayer.turn();

                    Exercise4.logMessage("Home " + turnPlayer.getName() + ": " + turnPlayer.getHouse().size());
                    Exercise4.logMessage("End " + turnPlayer.getName() + ": " + game.getGamePlan().getHouses(turnPlayer));
                    Exercise4.logMessage("Figures " + turnPlayer.getName() + ": " + turnPlayer.getFiguresOnPlan());
                    Exercise4.logMessage("---------- Next turn ----------");

                }

                if (!game.isRunning()) {
                    Exercise4.logMessage("Game ended. " + game.getWinner().getName() + " won.");
                    int currentWinnerScore = scores.get(getDiceId(game.getWinner()));
                    scores.put(getDiceId(game.getWinner()), currentWinnerScore + 1);
                }

                Exercise4.logMessage("========================================");
                Exercise4.logMessage("Game ended.");
                Exercise4.logMessage("Dice 1: " + player1.getDice());
                Exercise4.logMessage("Dice 2: " + player2.getDice());
                Exercise4.logMessage("Scores: " + scores);
                Exercise4.logMessage("========================================");

            }
        }

        System.out.println();
        System.out.println();
        System.out.println("Here are the wins of each dice ordered by quantity:");

        HashMap<Integer, Integer> sorted = Utils.sortByValue(scores, false);
        int counter = 1;
        for (int diceId : sorted.keySet()) {
            System.out.println(counter+".) Dice " + (diceId+1) + " ("+sorted.get(diceId)+" Wins)");
            counter++;
        }




    }

    public static void logMessage(String message) {
        if (showDebug)
            System.out.println("[DGA] " + message);
    }

    private static int getDiceId(GamePlayer player) throws InterruptedException {

        for (int i = 0; i<dices.size(); i++) {
            if (player.getDice().toString().equals(Arrays.asList(dices.get(i).split(" ")).subList(1, dices.get(i).split(" ").length).toString())) {
                return i;
            }
        }

        return -1;
    }

}
