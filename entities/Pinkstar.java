package entities;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.IsFloor;

public class Pinkstar extends Enemy {

    private boolean preRoll = true;
    private int tickSinceLastDmgToPlayer;
    private int tickAfterRollInIdle;
    private int rollDurationTick, rollDuration = 300;

    public Pinkstar(float x, float y) {
        super(x, y, PINKSTAR_WIDTH, PINKSTAR_HEIGHT, PINKSTAR);
        initHitbox(17, 21);
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
                    preRoll = true;
                    if (tickAfterRollInIdle >= 120) {
                        if (IsFloor(hitbox, lvlData))
                            newState(RUNNING);
                        else
                            inAir = true;
                        tickAfterRollInIdle = 0;
                        tickSinceLastDmgToPlayer = 60;
                    } else
                        tickAfterRollInIdle++;
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardPlayer(player);
                        newState(ATTACK);
//                        setWalkDir(player);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (preRoll) {
                        if (aniIndex >= 3)
                            preRoll = false;
                    } else {
                        move(lvlData);
                        checkDmgToPlayer(player);
                        checkRollOver();
                    }
                    break;
                case HIT:
                    tickAfterRollInIdle = 120;
                    break;
                case DEAD:
                    break;
            }
        }
    }

    private void checkDmgToPlayer(Player player) {
        if (hitbox.intersects(player.getHitbox())) {
            if (tickSinceLastDmgToPlayer >= 60) {
                tickSinceLastDmgToPlayer = 0;
                player.changeHealth(-GetEnemyDmg(PINKSTAR));
            } else
                tickSinceLastDmgToPlayer++;
        }
    }

//    private void setWalkDir(Player player) {
//        if (player.hitbox.x > hitbox.x)
//            walkDir = RIGHT;
//        else
//            walkDir = LEFT;
//    }

    private void checkRollOver() {
        rollDurationTick++;
        if (rollDurationTick >= rollDuration) {
            newState(IDLE);
            rollDurationTick = 0;
        }
    }

    @Override
    protected void move(int[][] lvlData) {
        float xSpeed = 0;
        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (state == ATTACK)
            xSpeed *= 2;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        }
        if (state == ATTACK) {
            newState(IDLE);
            rollDurationTick = 0;
        }
        changeWalkDir();
    }
}
