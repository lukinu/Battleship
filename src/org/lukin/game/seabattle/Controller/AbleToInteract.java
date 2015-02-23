package org.lukin.game.seabattle.Controller;

import org.lukin.game.seabattle.Model.*;

public interface AbleToInteract {
    public void drawField(Field ownField, Field foesField, Player whoseTurn);

    public String inputPlayerName();

    public boolean isAutoPlacement();

    public void outputMessage(String message);

    public Cell getShot(Field fieldToShoot);

    public Cell getRandomShot(Computer computer);

}
