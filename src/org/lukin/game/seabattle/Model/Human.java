package org.lukin.game.seabattle.Model;

import java.io.Serializable;

public class Human extends Player implements Serializable {
    private static final long serialVersionUID = 1L;

    public Human(String name, boolean isAuto) {
        super(name);
        setAutoPlacement(isAuto);
    }

    public boolean shoot(Cell cell) {
        boolean isHit = false;
        if (cell.getCellState().equals(SeaBattleConstants.HIT))
            return isHit;
        if (cell.getCellState().equals(SeaBattleConstants.SHIP)) {
            cell.setCellState(SeaBattleConstants.HIT);
            cell.getShip().delCellFromShip(cell);
            isHit = true;
        } else
            cell.setCellState(SeaBattleConstants.MISS);
        return isHit;
    }
}
