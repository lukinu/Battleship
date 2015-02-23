package org.lukin.game.seabattle.Model;

import java.io.Serializable;

public abstract class Player implements Shootable, Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private boolean AutoPlacement;
    protected Field fieldToShoot;

    public final Field getFieldToShoot() {
        return fieldToShoot;
    }

    public void setFieldToShoot(Field fieldToShoot) {
        if (fieldToShoot.getOwner() != this)
            this.fieldToShoot = fieldToShoot;
        else
            System.out.println("can't shoot yourself! setFieldToShoot skipped.");
    }

    public final boolean isAutoPlacement() {
        return AutoPlacement;
    }

    public void setAutoPlacement(boolean isAutoPlacement) {
        this.AutoPlacement = isAutoPlacement;
    }

    public Player(String name) {
        this.name = name;
        AutoPlacement = true;
    }

    public final String getName() {
        return name;
    }

    public abstract boolean shoot(Cell point);
}