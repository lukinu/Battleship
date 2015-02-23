package org.lukin.game.seabattle.View;

import org.lukin.game.seabattle.Model.*;

import java.util.Random;
import java.util.Scanner;

public class ConsoleView {

    public void drawField(Field ownField, Field foesField, Player whoseTurn) {
        System.out.println(ownField.getOwner().getName() + "'s field:" + "\t\t\t" + foesField.getOwner().getName() + "'s field:");
        System.out.println("   A B C D E F G H I K" + "\t" + "   A B C D E F G H I K");

        for (short i = 0; i < SeaBattleConstants.FIELD_SIZE; i++) {
            System.out.print(i + "| ");
            for (short j = 0; j < SeaBattleConstants.FIELD_SIZE; j++) {
                if (ownField.getCell(j, i).getCellState().equals(SeaBattleConstants.MISS)) {
                    System.out.print(SeaBattleConstants.EMPTY);
                    continue;
                }
                System.out.print(ownField.getCell(j, i).getCellState());
            }
            System.out.print("\t");
            System.out.print(i + "| ");
            for (short j = 0; j < SeaBattleConstants.FIELD_SIZE; j++) {
                if (foesField.getCell(j, i).getCellState().equals(SeaBattleConstants.SHIP)) {
                    System.out.print(SeaBattleConstants.EMPTY);
                    continue;
                }
                System.out.print(foesField.getCell(j, i).getCellState());
            }
            System.out.println();
        }
    }

    public String inputPlayerName() {
        // input player' name:
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter your name:");
        return scanner.nextLine();
    }

    public boolean isAutoPlacement() {
        // input placement mode:
        Scanner scanner = new Scanner(System.in);
        System.out.println("do you wanna place your ships automatically? (y)");
        if (scanner.next().equals("y"))
            return true;
        else
            return false;
    }

    public void outputMessage(String message) {
        System.out.println(message);
    }

    public FieldPoint getShot() {
        short x;
        short y;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("please input coordinates. \n X:");
            x = scanner.nextShort();
        }
        while ((x > 9) || (x < 0));
        do {
            System.out.println("Y:");
            y = scanner.nextShort();
        }
        while ((y > 9) || (y < 0));
        return new FieldPoint(x, y);
    }

    public FieldPoint getRandomShot() {
        Random rand = new Random();
        return new FieldPoint((short) rand.nextInt(SeaBattleConstants.FIELD_SIZE - 1), (short) rand.nextInt(SeaBattleConstants.FIELD_SIZE - 1));
    }
}