package org.lukin.game.seabattle.Controller;

import org.lukin.game.seabattle.Model.*;
import org.lukin.game.seabattle.View.GuiView;

import java.io.Serializable;

public class GuiController implements AbleToInteract, Serializable {
    private transient GuiView View = new GuiView();

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
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Cell shootPoint = (View.isSmartMode()) ? computer.getSmartCell() : computer.getRandomCell();
        return shootPoint;
    }
}