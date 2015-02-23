package org.lukin.game.seabattle.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Ship implements Serializable {
    private static final long serialVersionUID = 1L;
    // total counter of Ship objects created
    private static short totalAmountOfShips = 0;
    private ShipType type;
    private short size = 0;
    private Field field;
    // ship itself consists of cells
    private ArrayList<Cell> shipCells = new ArrayList<Cell>(size);

    private boolean isDead = false;

    public enum ShipType {
        BATTLESHIP((short) 4),
        CRUISER((short) 3),
        DESTROYER((short) 2),
        SUBMARINE((short) 1);

        private final short type;

        ShipType(short type) {
            this.type = type;
        }

        public short getValue() {
            return type;
        }
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    // how many ships do exist?
    public static short getTotalAmountOfShips() {
        return totalAmountOfShips;
    }

    public final short getSize() {
        return size;
    }

    public boolean isDead() {
        return isDead;
    }

    // what type is the ship?
    public final ShipType getType() {
        return type;
    }

    // constructor with ship type
    public Ship(ShipType type) {
        this.type = type;
        size = type.getValue();
        totalAmountOfShips++;
        if (SeaBattleConstants.DEBUG)
            System.out.println("A new " + type + " " + this + " has been added");
    }

    public void addCellToShip(Cell cell) {
        cell.setShip(this);
        shipCells.add(cell);
    }

    public void delCellFromShip(Cell cell) {
        cell.setShip(null);
        shipCells.remove(cell);
        if (shipCells.isEmpty()) {
            field.delShipFromField(this);
            isDead = true;
        }
    }
}