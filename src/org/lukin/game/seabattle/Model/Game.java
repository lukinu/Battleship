package org.lukin.game.seabattle.Model;

import org.lukin.game.seabattle.Controller.AbleToInteract;
import org.lukin.game.seabattle.Controller.ConsoleController;
import org.lukin.game.seabattle.Controller.GuiController;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Game {
    public static final Object key = new Object();
    public static final Object restartKey = new Object();
    private Map<String, Player> players;
    private AbleToInteract userInterface;
    // array of player's own battlefields
    private HashMap<String, Field> fields;
    private static boolean doesUseGui = true;

    public Game() {
        userInterface = (doesUseGui) ? new GuiController() : new ConsoleController();
        players = new HashMap<String, Player>();
        fields = new HashMap<String, Field>();
        players.put("Human", new Human(userInterface.inputPlayerName(), true));
        // second player is a robot
        players.put("Computer", new Computer());
        // create fields and fill them with ships
        fields.put("Human's", new Field(players.get("Human")));
        fields.put("Computer's", new Field(players.get("Computer")));
        // setting the aliens
        players.get("Human").setFieldToShoot(fields.get("Computer's"));
        players.get("Computer").setFieldToShoot(fields.get("Human's"));
    }

    // run the game
    public void run() throws InterruptedException {
        while (true) {
            Cell shot;
            Ship aShip;
            boolean isGameOver = false;
            String whoseTurn = "Human";
            userInterface.outputMessage("Welcome to the Battleship, " + players.get("Human").getName() + "!");
            // run game cycle
            do {
                userInterface.drawField(fields.get("Human's"), fields.get("Computer's"), players.get(whoseTurn));
                if (players.get(whoseTurn).getName().equals("Computer")) {
                    shot = userInterface.getRandomShot((Computer) players.get("Computer"));
                } else {
                    synchronized (key) {
                        try {
                            key.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        shot = userInterface.getShot(players.get(whoseTurn).getFieldToShoot());
                    }
                }
                aShip = shot.getShip();
                if (players.get(whoseTurn).shoot(shot)) {
                    if (aShip.isDead())
                        userInterface.outputMessage("Sunk!!");
                    else
                        userInterface.outputMessage("Hit!");
                    if (players.get(whoseTurn).getFieldToShoot().isEmpty()) {
                        userInterface.outputMessage("Player " + players.get(whoseTurn).getName() + " won! Congratulations!");
                        isGameOver = true;
                    }
                } else {
                    userInterface.outputMessage(" ");
                    whoseTurn = (whoseTurn.equals("Human")) ? "Computer" : "Human";
                }
            } while (!isGameOver);
            userInterface.drawField(fields.get("Human's"), fields.get("Computer's"), players.get(whoseTurn));
            synchronized (restartKey) {
                try {
                    restartKey.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void restartGame() throws InterruptedException {
        for (Field field : fields.values())
            field.init();
        players.get("Computer").setFieldToShoot(fields.get("Human's"));
        userInterface.outputMessage("Welcome to the Battleship, " + players.get("Human").getName() + "!");
        userInterface.drawField(fields.get("Human's"), fields.get("Computer's"), players.get("Human"));
    }

    public void writeGame(File file) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        objectOutputStream.writeObject(players);
        objectOutputStream.writeObject(fields);
        userInterface.outputMessage("game saved");
        objectOutputStream.close();
    }

    public void readGame(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        players = (HashMap<String, Player>) objectInputStream.readObject();
        fields = (HashMap<String, Field>) objectInputStream.readObject();
        objectInputStream.close();
        userInterface.drawField(fields.get("Human's"), fields.get("Computer's"), players.get("Human"));
        userInterface.outputMessage("game restored");
    }
}