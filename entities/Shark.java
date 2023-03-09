package entities;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.IsFloor;

public class Shark extends Enemy {

    public Shark(float x, float y) {
        super(x, y, SHARK_WIDTH, SHARK_HEIGHT, SHARK);
        initHitbox(18, 22);
        initAttackBox(20, 20, 20);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick(player);
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
                    else if (aniIndex == 3) {
                        checkPlayerHit(attackBox, player);
                        attackMove(lvlData);
                    }
                    break;
                case HIT:
                    break;
                case DEAD:
                    break;
            }
        }
    }

    private void attackMove(int[][] lvlData) {
        float xSpeed = 0;

        if (walkDir == LEFT)
            xSpeed -= walkSpeed;
        else
            xSpeed += walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed * 3, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if (IsFloor(hitbox, xSpeed * 3, lvlData)) {
                hitbox.x += xSpeed * 3;
                return;
            }
        }
        newState(IDLE);
    }
}
