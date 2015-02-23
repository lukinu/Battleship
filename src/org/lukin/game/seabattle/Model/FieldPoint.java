package org.lukin.game.seabattle.Model;

import java.io.Serializable;

public class FieldPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    short x;

    public FieldPoint(short x, short y) {
        this.x = x;
        this.y = y;
    }

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    short y;

}
