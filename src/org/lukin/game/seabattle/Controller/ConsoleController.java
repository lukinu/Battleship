package org.lukin.game.seabattle.Controller;

import org.lukin.game.seabattle.Model.*;
import org.lukin.game.seabattle.View.ConsoleView;

import java.io.Serializable;

public class ConsoleController implements AbleToInteract, Serializable {
    ConsoleView View = new ConsoleView();

    @Override
    public void drawField(Field ownField, Field foesField, Player whoseTurn) {
        View.drawField(ownField, foesField, whoseTurn);
    }

    @Override
    public String inputPlayerName() {
        return View.inputPlayerName();
    }

    @Override
    public boolean isAutoPlacement() {
        return View.isAutoPlacement();
    }

    @Override
    public void outputMessage(String message) {
        View.outputMessage(message);
    }

    @Override
    public Cell getShot(Field fieldToShoot) {
        FieldPoint shootPoint = View.getShot();
        return fieldToShoot.getCell(shootPoint.getX(), shootPoint.getY());
    }

    @Override
    public Cell getRandomShot(Computer computer) {
        Field fieldToShoot = computer.getFieldToShoot();
        FieldPoint shootPoint = View.getRandomShot();
        return fieldToShoot.getCell(shootPoint.getX(), shootPoint.getY());
    }
}