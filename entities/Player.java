package entities;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;

    public boolean moving = false, attacking = false, powerAttack1 = false, powerAttack2 = false, powerAttack3 = false;
    public boolean left, right, jump;
    private int[][] levelData;
    private float xDrawOffset = 17 * Game.SCALE;
    private float yDrawOffset = 6 * Game.SCALE;

    // Jumping / Gravity
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    // Status Bar UI
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);
    private int healthWidth = healthBarWidth;

    private int powerBarWidth = (int) (104 * Game.SCALE);
    private int powerBarHeight = (int) (2 * Game.SCALE);
    private int powerBarXStart = (int) (44 * Game.SCALE);
    private int powerBarYStart = (int) (34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 100;
    private int powerValue = powerMaxValue;
    private int normalAttackValue = 5;


    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;

    private int tileY = 0;
    private int powerAttackTick;
    //    private int powerGrowSpeed = 60;
    private int powerGrowSpeed = 1;

    private int normalAttackTick;
    private int normalAttackGrowSpeed = 50;
    private int normalAttackGrowTick;
    private int powerGrowTick;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.walkSpeed = 1f * Game.SCALE;
//        this.maxHealth = 30;
        this.maxHealth = 30000;

        this.currentHealth = maxHealth;
        loadAnimations();
        initHitbox(20, 30);
        initAttackBox();
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (30 * Game.SCALE), (int) (20 * Game.SCALE));
        resetAttackBox();
    }

    public void update() {
        updateHealthBar();
        updatePowerBar();
        updateNormalAttack();

        if (currentHealth <= 0) {
            if (state != DEAD) {
                state = DEAD;
                aniTick = 0;
                aniIndex = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            } else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1) {
                playing.setGameOver(true);
                Enemy.count = 0;
                Player.k = 0;
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            } else updateAnimationTick();

            return;
        }

        updateAttackBox();
        updatePos();

        if (moving) {
            checkPotionTouched();
            checkSpikeTouched();
            checkInsideWater();
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
        if (attacking || powerAttack1 || powerAttack2 || powerAttack3) checkAttack();

        updateAnimationTick();
        setAnimation();
    }

    private void checkInsideWater() {
        if (IsEntityInWater(hitbox, playing.getLevelManager().getCurrentLevel().getLevelData())) currentHealth = 0;
    }

    private void checkSpikeTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if (attackChecked) return;

        attackChecked = true;

        if (powerAttack1 || powerAttack2 || powerAttack3)
            attackChecked = false; // make sure to check attack for every update

        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void setAttackBoxOnRightSide() {

        attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 2);
    }

    private void setAttackBoxOnLeftSide() {
        attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 2);
    }

    private void updateAttackBox() {
        if (right && left) {
            if (flipW == 1) {
                setAttackBoxOnRightSide();
            } else {
                setAttackBoxOnLeftSide();
            }
        } else if (right || ((powerAttack1 || powerAttack2 || powerAttack3) && flipW == 1)) {
            setAttackBoxOnRightSide();
        } else if (left || ((powerAttack1 || powerAttack2 || powerAttack3) && flipW == -1)) {
            setAttackBoxOnLeftSide();
        }
        attackBox.y = hitbox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    private void updatePowerBar() {
        powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);
        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1);
        }
    }

    private void updateNormalAttack() {
        normalAttackGrowTick++;
        if (normalAttackGrowTick >= normalAttackGrowSpeed) {
            normalAttackGrowTick = 0;
            normalAttackValue++;
        }

    }

    public void render(Graphics g, int xlvlOffset) {

        g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - xlvlOffset + flipX, (int) (hitbox.y - yDrawOffset - 10 * Game.SCALE), width * flipW, (int) (50 * Game.SCALE), null);

//        drawHitbox(g, xlvlOffset);
//        drawAttackBox(g, xlvlOffset);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        // Background UI
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        // Health bar
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

        // Power bar
        g.setColor(Color.YELLOW);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (state == IDLE_2) ANI_SPEED = 50;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
                attacking = false;
                powerAttack1 = false;
                powerAttack2 = false;
                powerAttack3 = false;
                attackChecked = false;
                if (state == HIT) {
                    newState(IDLE);
                    airSpeed = 0f;
                    if (!IsFloor(hitbox, 0, playing.getLevelManager().getCurrentLevel().getLevelData())) {
                        inAir = true;
                    }
                }
            }
        }
        ANI_SPEED = 20;
    }

    public boolean s;

    public static int k = 0;

    private void setAnimation() {
        boolean c = true;
        int startAni = state;
        if (state == HIT) {
            return;
        }
        if (moving && c) state = RUNNING;
        else state = IDLE;
        if (right && !left) {
            s = true;

        } else if (left && !right) {
            s = false;

        }

        if (inAir) {
            if (airSpeed < 0) state = JUMP;
            else state = RUNNING;
        }
        if (Enemy.count >= 3 && k == 0) {
            c = false;
            state = IDLE_2;
            if (startAni != IDLE_2) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
            if (aniIndex == 8) k++;
        }
        if (Enemy.count >= 10 && k == 1) {
            c = false;
            state = IDLE_2;
            if (startAni != IDLE_2) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
            if (aniIndex == 8) k++;
        }
//        System.out.println(k);
//        System.out.println(Enemy.count);
        if (powerAttack1 && state != PowerAttack_2 && state != PowerAttack_3 && c) {
            state = PowerAttack_1;
            if (startAni != PowerAttack_1) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }
        if (state != PowerAttack_1 && powerAttack2 && state != PowerAttack_3 && c && (k == 1 || k == 2)) {
            state = PowerAttack_2;
            if (startAni != PowerAttack_2) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }
        if (state != PowerAttack_1 && state != PowerAttack_2 && powerAttack3 && c && k == 2) {
            state = PowerAttack_3;
            if (startAni != PowerAttack_3) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }
        if (!powerAttack1 && !powerAttack2 && !powerAttack3 && attacking && c) {
            state = ATTACK_1;
            if (startAni != ATTACK_1) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }


        if (startAni != state) resetAniTick();

    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;

        if (jump) jump();

        if (!inAir)
            if (!powerAttack1 && !powerAttack3 && !powerAttack2) if ((!left && !right) || (left && right)) return;

        float xSpeed = 0;

        if (left && !right) {
            xSpeed = -walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right && !left) {
            xSpeed = walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (powerAttack1 || powerAttack2) {
            if ((!left && !right) || (left && right)) {
                if (flipW == -1) {
                    xSpeed = -walkSpeed;
                } else {
                    xSpeed = walkSpeed;
                }
            }
            if (powerAttack2) {
                xSpeed *= 0.1f;
            }
            xSpeed *= 3; // 3 times the speed
        }

        if (!inAir) if (!IsEntityOnFloor(hitbox, levelData)) inAir = true;

        if (inAir && !powerAttack1 && !powerAttack2 && !powerAttack3) { // not updating Y while in power attack
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) resetInAir();
                else airSpeed = fallSpeedAfterCollision;

                updateXPos(xSpeed);
            }

        } else updateXPos(xSpeed);

        moving = true;
    }

    private void jump() {
        if (inAir) {
            return;
        }
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0f;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
            if (attacking) {
                attacking = false;
                normalAttackTick = 0;
            }
            if (powerAttack1) {
                powerAttack1 = false;
                powerAttackTick = 0;
            }
            if (powerAttack2) {
                powerAttack2 = false;
                powerAttackTick = 0;
            }
            if (powerAttack3) {
                powerAttack3 = false;
                powerAttackTick = 0;
            }
        }
    }

    public void changeHealth(int value) {
        if (value <= 0) {
            if (state == HIT) return;
            else newState(HIT);
        }

        currentHealth += value;
        currentHealth = Math.max(Math.min(currentHealth, maxHealth), 0);
    }

    public void kill() {
        currentHealth = 0;
    }

    public void changePower(int value) {
        powerValue += value;
        powerValue = Math.max(Math.min(powerValue, powerMaxValue), 0);
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[11][12];
        for (int i = 0; i < animations.length; i++)
            for (int j = 0; j < animations[i].length; j++)
                animations[i][j] = img.getSubimage(j * 96, i * 60, 96, 60);

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] levelData) {
        this.levelData = levelData;
        if (!IsEntityOnFloor(hitbox, levelData)) inAir = true;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        jump = false;
        attacking = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetInAir();
        resetDirBooleans();
        attacking = false;
        moving = false;
        newState(IDLE);
        currentHealth = maxHealth;
        powerAttack1 = false;
        powerAttack2 = false;
        powerAttack3 = false;
        powerAttackTick = 0;
        normalAttackTick = 0;
        normalAttackValue = 5;
        powerValue = powerMaxValue;
        hitbox.x = x;
        hitbox.y = y;
        resetAttackBox();

        if (!IsEntityOnFloor(hitbox, levelData)) inAir = true;
    }

    private void resetAttackBox() {
        if (flipW == 1) setAttackBoxOnRightSide();
        else setAttackBoxOnLeftSide();
    }

    public int getTileY() {
        return tileY;
    }

    public void powerAttack() {
        if (powerAttack1) return;
        if (powerValue >= 25) {
            powerAttack1 = true;
            changePower(-25);
        }
    }

    public void normalAttack() {
        if (attacking) return;
        if (normalAttackValue >= 5) {
            attacking = true;
            normalAttackValue = 0;
        }
    }

    public void powerAttack_2() {
        if (powerAttack2) return;
        if (powerValue >= 33 && (k == 1 || k == 2)) {
            powerAttack2 = true;
            changePower(-33);
        }
    }

    public void powerAttack_3() {
        if (powerAttack3) return;
        if (powerValue >= 50 && k == 2) {
            powerAttack3 = true;
            changePower(-50);
        }
    }

    public int getanitick() {
        return aniTick;
    }
}
