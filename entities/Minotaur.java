package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.IsFloor;

public class Minotaur extends Enemy {

    private int attackBoxOffsetX;

    public Minotaur(float x, float y) {
        super(x, y, MINOTAUR_WIDTH, MINOTAUR_HEIGHT, MINOTAUR);
        initHitbox(20, 30);
        initAttackBox(41, 19, 30);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick(player);
        MinotaurUpdateAttackBox();
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (state) {
                case IDLE:
                    if (IsFloor(hitbox, lvlData))
                        newState(RUNNING);
                    else
                        inAir = true;
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;

                    if (aniIndex == 8 ) {
                        checkPlayerHit(attackBox, player);
                    }
                    break;
                case DEAD:
                    break;
            }
        }
    }
//    public int flipX() {
//        if (walkDir == LEFT)
//            return (int) (width - (30 * Game.SCALE));
//        else
//            return 0;
//    }
//
//    public int flipW() {
//        if (walkDir == LEFT)
//            return -1;
//        else
//            return 1;
//    }
}