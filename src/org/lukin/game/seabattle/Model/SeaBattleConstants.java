package org.lukin.game.seabattle.Model;

public class SeaBattleConstants {
    public static final boolean DEBUG = false;
    public static final short FIELD_SIZE = 10;
    public static final String EMPTY = "\u25a1 ";
    public static final String HIT = "\u22a0 ";
    public static final String MISS = "\u2022 ";
    public static final String SHIP = "\u25a0 ";
    public static final short SUBMARINES_TOTAL = 4;
    public static final short DESTROYERS_TOTAL = 3;
    public static final short CRUISERS_TOTAL = 2;
    public static final short BATTLESHIPS_TOTAL = 1;
    public static final short CELL_SIZE = 20;
    public static final short SHIPS_TOTAL = SUBMARINES_TOTAL + DESTROYERS_TOTAL + CRUISERS_TOTAL + BATTLESHIPS_TOTAL;
}
