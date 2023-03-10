package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.IsFloor;

public class Golem extends Enemy {

    private int attackBoxOffsetX;

    public Golem(float x, float y) {
        super(x, y, GOLEM_WIDTH, GOLEM_HEIGHT, GOLEM);
        initHitbox(22, 30);
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
                    if (aniIndex == 4 ) {
                        checkPlayerHit(attackBox, player);
                        attackMove(lvlData);
                    }
                    break;
                case DEAD:
                    break;
            }
        }
    }
    private void attackMove(int[][] lvlData) {
        float xSpeed = 0;
        if (walkDir == LEFT)
            xSpeed -= (int) (walkSpeed +1 * Game.SCALE);
        else
            xSpeed += (int) (walkSpeed + 1 * Game.SCALE);
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += (int) (xSpeed * 2 * Game.SCALE);
                return;
            }
        }
        newState(IDLE);
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