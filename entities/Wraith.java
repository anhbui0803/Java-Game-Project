package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.IsFloor;

public class Wraith extends Enemy {

    public static boolean ef = false;
    private int attackBoxOffsetX;

    public Wraith(float x, float y) {
        super(x, y, WRAITH_WIDTH, WRAITH_HEIGHT, WRAITH);
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
                        if (isPlayerCloseForAttack(player)) {
                            newState(ATTACK);

                        }
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex == 17) {
                        ef = true;
                    } else ef = false;
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