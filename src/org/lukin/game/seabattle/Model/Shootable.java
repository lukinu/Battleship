package org.lukin.game.seabattle.Model;

public interface Shootable {
    public boolean shoot(Cell point);

    public void setFieldToShoot(Field fieldToShoot);
}
