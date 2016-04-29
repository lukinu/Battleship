package org.lukin.game.seabattle.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Computer extends Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Cell> groupToShoot = new ArrayList<Cell>();
    private List<Cell> smartModeCells = new ArrayList<Cell>();

    Computer() {
        super("Computer");
    }

    public void setFieldToShoot(Field fieldToShoot) {
        super.setFieldToShoot(fieldToShoot);
        smartModeCells.clear();
        for (Cell cell : fieldToShoot.getField()) {
            smartModeCells.add(cell);
        }
    }

    public boolean shoot(Cell cell) {
        boolean isHit = false;
        Ship hitedShip = cell.getShip();
        if (cell.getCellState().equals(SeaBattleConstants.HIT))
            return isHit;
        if (hitedShip != null) {
            cell.setCellState(SeaBattleConstants.HIT);
            hitedShip.delCellFromShip(cell);
            for (Cell neighbourCell : cell.getNeighbourCells()) {
                smartModeCells.remove(neighbourCell);
                if (SeaBattleConstants.DEBUG)
                    neighbourCell.setCellState(SeaBattleConstants.MISS);
            }
            groupToShoot.clear();
            groupToShoot = getGroupToShoot(cell);
            //if sunk then do not shoot surrounding cells
            if (hitedShip.isDead()) {
                for (Cell cellToShoot : groupToShoot) {
                    smartModeCells.remove(cellToShoot);
                    if (SeaBattleConstants.DEBUG)
                        cellToShoot.setCellState(SeaBattleConstants.MISS);
                }
                groupToShoot.clear();
            }
            isHit = true;
        } else {
            cell.setCellState(SeaBattleConstants.MISS);
        }
        smartModeCells.remove(cell);
        return isHit;
    }

    private ArrayList<Cell> getGroupToShoot(Cell cell) {
        ArrayList<Cell> groupToShoot = new ArrayList<Cell>();
        Cell littleEndCell;
        Cell bigEndCell;
        LinkedList<Cell> hitedGroup = cell.getHitedGroup();
        if (hitedGroup.size() != 0) {
            if (hitedGroup.size() == 1) {
                for (Cell adjacentCell : cell.getAdjacentCells())
                    if (!adjacentCell.getCellState().equals(SeaBattleConstants.MISS))
                        groupToShoot.add(adjacentCell);
            } else {
                if (hitedGroup.get(0).getX() == hitedGroup.getLast().getX()) {
                    if ((littleEndCell = fieldToShoot.getCell(hitedGroup.get(0).getX(), (short) (hitedGroup.get(0).getY() - 1))) != null)
                        groupToShoot.add(littleEndCell);
                    if ((bigEndCell = fieldToShoot.getCell(hitedGroup.getLast().getX(), (short) (hitedGroup.getLast().getY() + 1))) != null)
                        groupToShoot.add(bigEndCell);
                } else {
                    if ((littleEndCell = fieldToShoot.getCell((short) (hitedGroup.get(0).getX() - 1), hitedGroup.get(0).getY())) != null)
                        groupToShoot.add(littleEndCell);
                    if ((bigEndCell = fieldToShoot.getCell((short) (hitedGroup.getLast().getX() + 1), hitedGroup.getLast().getY())) != null)
                        groupToShoot.add(bigEndCell);
                }
            }
        }
        return groupToShoot;
    }

    public Cell getSmartCell() {
        if (groupToShoot.size() != 0)
            return groupToShoot.remove(0);
        else {
            int size = smartModeCells.size();
            Random rand = new Random();
            return smartModeCells.get(rand.nextInt(size));
        }
    }

    public Cell getRandomCell() {
        Random rand = new Random();
        return fieldToShoot.getCell((short) rand.nextInt(SeaBattleConstants.FIELD_SIZE - 1), (short) rand.nextInt(SeaBattleConstants.FIELD_SIZE - 1));
    }
}