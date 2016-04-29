package org.lukin.game.seabattle.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Cell> field = new ArrayList<Cell>(SeaBattleConstants.FIELD_SIZE * SeaBattleConstants.FIELD_SIZE);
    private List<Ship> ships = new ArrayList<Ship>(SeaBattleConstants.SHIPS_TOTAL);
    private Player owner;

    private class Placement {
        short x;
        short y;
        boolean direction; // true = horiz./ false = vert.
    }

    public Field(Player owner) {
        this.owner = owner;
        Cell cell;
        for (int i = 0; i < SeaBattleConstants.FIELD_SIZE * SeaBattleConstants.FIELD_SIZE; i++) {
            cell = new Cell();
            field.add(cell);
            cell.setField(this);
        }
        init();
    }

    // init the field and fill it with ships
    public void init() {
        for (Cell cell : field) {
            cell.setCellState(SeaBattleConstants.EMPTY);
            cell.setShip(null);
        }
        ships.clear();

        for (int i = 0; i < SeaBattleConstants.BATTLESHIPS_TOTAL; i++) {
            placeShip(new Ship(Ship.ShipType.BATTLESHIP));
        }
        for (int i = 0; i < SeaBattleConstants.CRUISERS_TOTAL; i++) {
            placeShip(new Ship(Ship.ShipType.CRUISER));
        }
        for (int i = 0; i < SeaBattleConstants.DESTROYERS_TOTAL; i++) {
            placeShip(new Ship(Ship.ShipType.DESTROYER));
        }
        for (int i = 0; i < SeaBattleConstants.SUBMARINES_TOTAL; i++) {
            placeShip(new Ship(Ship.ShipType.SUBMARINE));
        }
    }

    public List<Cell> getField() {
        return field;
    }

    // placing a ship onto the battlefield
    public boolean placeShip(Ship ship) {
        short length = ship.getSize();
        short x;
        short y;
        Cell tempCell;
        Placement placement;
        do {
            placement = owner.isAutoPlacement() ? getRandomPlace(length) : getPlace(length);
        }
        while (!isPlacementValid(placement, length));

        x = placement.x;
        y = placement.y;

        for (int k = 0; k < length; k++) {
            tempCell = field.get(x * SeaBattleConstants.FIELD_SIZE + y);
            tempCell.setCellState(SeaBattleConstants.SHIP);
            ship.addCellToShip(tempCell);
            if (placement.direction)
                x++;
            else
                y++;
        }
        ships.add(ship);
        ship.setField(this);
        if (SeaBattleConstants.DEBUG)
            System.out.println("Ship " + ship.getType() + " " + ship + " has been placed to the field " + field);
        return true;
    }

    private boolean isPlacementValid(Placement placement, short length) {
        boolean isValid = true;
        short x = placement.x;
        short y = placement.y;
        // move around each cell and check if it is free yet
        for (short k = 0; k < length; k++) {
            // is the cell or its adjacent cells occupied?
            for (short i = -1; i < 2; i++) {
                // is current cell out of vertical range?
                if ((y + i) < 0) continue;
                for (short j = -1; j < 2; j++) {
                    // is current cell out of horizontal range?
                    if ((x + j) < 0) continue;
                    // checking cell content
                    if (!field.get((x + j) * SeaBattleConstants.FIELD_SIZE + (y + i)).getCellState().equals(SeaBattleConstants.EMPTY)) {
                        isValid = false;
                        return isValid;
                    }
                }
            }
            //  shift focus one cell right or down
            if (placement.direction)
                x++;
            else
                y++;
        }
        return isValid;
    }

    // get random position and orientation
    private Placement getRandomPlace(short shift) {
        Placement placement = new Placement();
        Random rand = new Random();
        placement.x = (short) rand.nextInt(SeaBattleConstants.FIELD_SIZE - shift);
        placement.y = (short) rand.nextInt(SeaBattleConstants.FIELD_SIZE - shift);
        placement.direction = ((short) rand.nextInt(2) == 0);
        return placement;
    }

    // get position and orientation manually
    private Placement getPlace(short shift) {
        return getRandomPlace(shift); //todo
    }

    public Cell getCell(short x, short y) {
        if ((x >= 0) && (x < SeaBattleConstants.FIELD_SIZE) && (y >= 0) && (y < SeaBattleConstants.FIELD_SIZE))
            return field.get(y * SeaBattleConstants.FIELD_SIZE + x);
        else
            return null;
    }

    public Player getOwner() {
        return owner;
    }

    public void delShipFromField(Ship ship) {
        ships.remove(ship);
    }

    public boolean isEmpty() {
        return ships.isEmpty();
    }
}