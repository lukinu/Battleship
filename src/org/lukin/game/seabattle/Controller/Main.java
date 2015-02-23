package org.lukin.game.seabattle.Controller;

import org.lukin.game.seabattle.Model.Field;
import org.lukin.game.seabattle.Model.Game;
import org.lukin.game.seabattle.Model.Player;

import java.io.*;
import java.util.HashMap;

public class Main {
    private static Game game;

    public static void main(String[] args) throws InterruptedException {
        // run the game!
        game = new Game();
        game.run();
    }

    public static void writeGame(File file) throws IOException {
        game.writeGame(file);
    }

    public static void readGame(File file) throws IOException, ClassNotFoundException {
        game.readGame(file);
    }

    public static void restartGame() throws InterruptedException {
        game.restartGame();
    }
}