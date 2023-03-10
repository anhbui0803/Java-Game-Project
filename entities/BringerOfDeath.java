package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.IsFloor;

public class BringerOfDeath extends Enemy {

    public BringerOfDeath(float x, float y) {
        super(x, y, BRINGER_OF_DEATH_WIDTH, BRINGER_OF_DEATH_HEIGHT, BRINGER_OF_DEATH);
        initHitbox(32, 57);
        initAttackBox(90, 57, 60);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick(player);
        updateAttackBox();
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
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
                    else if (aniIndex == 4 && !attackChecked)
                        checkPlayerHit(attackBox, player);
                    break;
                case HIT:
                    break;
                case DEAD:
                    break;
            }
        }
    }
}
