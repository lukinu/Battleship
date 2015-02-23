package org.lukin.game.seabattle.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cellState;
    private Ship ship = null;
    private Field field;

    public Cell() {
        cellState = SeaBattleConstants.EMPTY;
    }

    public String getCellState() {
        return cellState;
    }

    public void setCellState(String cellState) {
        this.cellState = cellState;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public String toString() {
        return cellState;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public short getX() {
        return (short) (field.getField().indexOf(this) % SeaBattleConstants.FIELD_SIZE);
    }

    public short getY() {
        return (short) (field.getField().indexOf(this) / SeaBattleConstants.FIELD_SIZE);
    }

    public short getIdx() {
        return (short) field.getField().indexOf(this);
    }

    //  neighbour cells :
//    X X
//     0
//    X X
    public ArrayList<Cell> getNeighbourCells() {
        ArrayList<Cell> neighbourCells = new ArrayList<Cell>();
        Cell neighbourCell;
        short idx = (short) field.getField().indexOf(this);
        short x = (short) (idx % SeaBattleConstants.FIELD_SIZE);
        short y = (short) (idx / SeaBattleConstants.FIELD_SIZE);
        for (short i = -1; i < 2; i += 2) {
            for (short j = -1; j < 2; j += 2) {
                neighbourCell = field.getCell((short) (x + j), (short) (y + i));
                if (neighbourCell != null)
                    neighbourCells.add(neighbourCell);
            }
        }
        return neighbourCells;
    }

    //  adjacent cells :
//     X
//    X0X
//     X
    public ArrayList<Cell> getAdjacentCells() {
        ArrayList<Cell> adjacentCells = new ArrayList<Cell>();
        Cell adjacentCell;
        short idx = (short) field.getField().indexOf(this);
        short x = (short) (idx % SeaBattleConstants.FIELD_SIZE);
        short y = (short) (idx / SeaBattleConstants.FIELD_SIZE);
        for (short j = -1; j < 2; j += 2) {
            adjacentCell = field.getCell((short) (x + j), y);
            if (adjacentCell != null)
                adjacentCells.add(adjacentCell);
            adjacentCell = field.getCell(x, (short) (y + j));
            if (adjacentCell != null)
                adjacentCells.add(adjacentCell);
        }
        return adjacentCells;
    }

    public LinkedList<Cell> getHitedGroup() {
        LinkedList<Cell> hitedGroup = new LinkedList<Cell>();
        boolean isVertical = false;
        Cell nextCell;
        short nextX;
        short nextY;
        if (cellState.equals(SeaBattleConstants.HIT)) {
            hitedGroup.add(this);
            // find adjacent HIT and find get direction
            for (Cell adjacentCell : getAdjacentCells()) {
                if (adjacentCell.getCellState().equals(SeaBattleConstants.HIT)) {
                    isVertical = (getX() == adjacentCell.getX()) ? true : false;
                    if (adjacentCell.getIdx() > getIdx())
                        hitedGroup.add(adjacentCell);
                    else
                        hitedGroup.add(0, adjacentCell);
                    break;
                }
            }
            if (hitedGroup.size() > 1) {
                // testing little end
                do {
                    nextX = (isVertical) ? hitedGroup.get(0).getX() : (short) (hitedGroup.get(0).getX() - 1);
                    nextY = (isVertical) ? (short) (hitedGroup.get(0).getY() - 1) : hitedGroup.get(0).getY();
                    if ((nextX < 0) || (nextY < 0)) break;
                    if ((nextCell = field.getCell(nextX, nextY)).getCellState().equals(SeaBattleConstants.HIT))
                        hitedGroup.add(0, nextCell);
                } while (nextCell.getCellState().equals(SeaBattleConstants.HIT));
                //testing big end
                do {
                    nextX = (isVertical) ? hitedGroup.getLast().getX() : (short) (hitedGroup.getLast().getX() + 1);
                    nextY = (isVertical) ? (short) (hitedGroup.getLast().getY() + 1) : hitedGroup.get(0).getY();
                    if ((nextX > (SeaBattleConstants.FIELD_SIZE - 1)) || (nextY > (SeaBattleConstants.FIELD_SIZE - 1)))
                        break;
                    if ((nextCell = field.getCell(nextX, nextY)).getCellState().equals(SeaBattleConstants.HIT))
                        hitedGroup.add(nextCell);
                } while (nextCell.getCellState().equals(SeaBattleConstants.HIT));
            }
        }
        return hitedGroup;
    }
}