package objects;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Projectiles.*;

public class Skill {

    private Rectangle2D.Float hitbox;
    public int dir;
    private boolean active = true;

    public Skill(int x, int y, int dir) {
        int xOffset = (int) (-3 * Game.SCALE); // default for cannon facing left
        int yOffset = (int) (5 * Game.SCALE);

        if (dir == 1) {
            xOffset = (int) (29 * Game.SCALE);
        }

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, POWER_WIDTH, POWER_HEIGHT);
        this.dir = dir;
    }

    public void updatePos() {
        hitbox.x += dir * SPEED * 3;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public int getDir() {
        return dir;
    }
}
