package objects;

import entities.*;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.Projectiles.*;
import static utilz.HelpMethods.*;

public class ObjectManager {

    private Playing playing;
    private Wraith wraith;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage[] cannonImgs, grassImgs;
    private BufferedImage[][] treeImgs;
    private BufferedImage spikeImg, cannonBallImg, skillImg, effectImg;
    private ArrayList<Skill> skills = new ArrayList<>();
    private ArrayList<SpellsEffect> effects = new ArrayList<>();
    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<Spike> spikes = new ArrayList<>();
    private ArrayList<Cannon> cannons = new ArrayList<>();
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    private Level currentLevel;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        currentLevel = playing.getLevelManager().getCurrentLevel();
        loadImgs();
    }

    public void checkSpikeTouched(Player player) {
        for (Spike spike : spikes) {
            if (spike.getHitbox().intersects(player.getHitbox())) {
                player.kill();
            }
        }
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) { // check whether player touch the object or not
        for (Potion potion : potions) {
            if (potion.isActive()) {
                if (hitbox.intersects(potion.hitbox)) {
                    potion.setActive(false);
                    applyEffectToPlayer(potion);
                }
            }
        }
    }

    public void applyEffectToPlayer(Potion potion) {
        if (potion.getObjType() == RED_POTION) {
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        } else {
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
        }
    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer container : containers) {
            if (container.isActive() && !container.doAnimation) {
                if (container.getHitbox().intersects(attackbox)) {
                    container.setAnimation(true);
                    int type = 0;
                    if (container.getObjType() == BOX) {
                        type = 0;
                        potions.add(new Potion((int) (container.getHitbox().x + container.getHitbox().width / 2),
                                (int) (container.getHitbox().y - container.getHitbox().height / 1.28f),
                                type));
                    }
                    if (container.getObjType() == BARREL) {
                        type = 1;
                        potions.add(new Potion((int) (container.getHitbox().x + container.getHitbox().width / 2),
                                (int) (container.getHitbox().y - container.getHitbox().height / 3.65f),
                                type));
                    }
                    return;
                }
            }
        }
    }

    public void loadObjects(Level newLevel) {
        currentLevel = newLevel;
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes(); // spikes are static so no need to re-check
        cannons = newLevel.getCannons();
        projectiles.clear(); // clear when start new level
        skills.clear();
        effects.clear();
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];

        for (int i = 0; i < potionImgs.length; i++) {
            for (int j = 0; j < potionImgs[i].length; j++) {
                potionImgs[i][j] = potionSprite.getSubimage(12 * j, 16 * i, 12, 16);
            }
        }

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for (int i = 0; i < containerImgs.length; i++) {
            for (int j = 0; j < containerImgs[i].length; j++) {
                containerImgs[i][j] = containerSprite.getSubimage(40 * j, 30 * i, 40, 30);
            }
        }

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
        skillImg = LoadSave.GetSpriteAtlas(LoadSave.SKILL);
        effectImg = LoadSave.GetSpriteAtlas(LoadSave.EFFECT);

        BufferedImage cannonSprite = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
        cannonImgs = new BufferedImage[7];

        for (int i = 0; i < cannonImgs.length; i++) {
            cannonImgs[i] = cannonSprite.getSubimage(i * 40, 0, 40, 26);
        }

        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);

        treeImgs = new BufferedImage[2][4];
        BufferedImage treeOneImg = LoadSave.GetSpriteAtlas(LoadSave.TREE_ONE_ATLAS);
        for (int i = 0; i < 4; i++)
            treeImgs[0][i] = treeOneImg.getSubimage(i * 39, 0, 39, 92);

        BufferedImage treeTwoImg = LoadSave.GetSpriteAtlas(LoadSave.TREE_TWO_ATLAS);
        for (int i = 0; i < 4; i++)
            treeImgs[1][i] = treeTwoImg.getSubimage(i * 62, 0, 62, 54);

        BufferedImage grassTemp = LoadSave.GetSpriteAtlas(LoadSave.GRASS_ATLAS);
        grassImgs = new BufferedImage[2];
        for (int i = 0; i < grassImgs.length; i++)
            grassImgs[i] = grassTemp.getSubimage(32 * i, 0, 32, 32);
    }

    public void update(int[][] lvlData, Player player) {
        updateBackgroundTrees();
        for (Potion potion : potions) {
            if (potion.isActive()) {
                potion.update();
            }
        }

        for (GameContainer container : containers) {
            if (container.isActive()) {
                container.update();
            }
        }

        updateCannons(lvlData, player);
        updateAttack(lvlData, player);
        updateProjectile(lvlData, player);
        updateSkill(lvlData, player);
        updateEffect(lvlData, player);
        updateSpellsEffect(lvlData, player);
    }


    private void updateBackgroundTrees() {
        for (BackgroundTree bt : currentLevel.getTrees())
            bt.update();
    }

    private void updateProjectile(int[][] lvlData, Player player) {
        for (Projectile projectile : projectiles) {
            if (projectile.isActive()) {
                projectile.updatePos();
            }
            if (projectile.getHitbox().intersects(player.getHitbox()) && projectile.isActive()) {
                player.changeHealth(-3);
                projectile.setActive(false);
            } else if (IsProjectileHittingLevel(lvlData, projectile)) {
                projectile.setActive(false);
            }
        }
    }

    private void updateEffect(int[][] lvlData, Player player) {
        for (SpellsEffect effect : effects) {
            if (effect.isActive()) {
                effect.updatePos();
            }
            if (effect.getHitbox().intersects(player.getHitbox()) && effect.isActive()) {
                player.changeHealth(-6);
                effect.setActive(false);

            }
//            } else if (IsProjectileHittingLevel2(lvlData, effect)) {
//                effect.setActive(false);
//            }
        }
    }

    private void updateSkill(int[][] lvlData, Player player) {
        for (Skill skill : skills) {
            if (skill.isActive()) {
                skill.updatePos();
            }
            for (Monster m : currentLevel.getMonsters()) {
                if (skill.getHitbox().intersects(m.getHitbox()) && skill.isActive()) {
                    m.hurt(19);
                    skill.setActive(false);
                } else if (IsProjectileHittingLevel1(lvlData, skill)) {
//                    skill.setActive(false);
                }
            }
            for (Crabby c : currentLevel.getCrabs()) {
                if (skill.getHitbox().intersects(c.getHitbox()) && skill.isActive()) {
                    c.hurt(1);
//                    skill.setActive(false);
                } else if (IsProjectileHittingLevel1(lvlData, skill)) {
//                    skill.setActive(false);
                }
            }
            for (Minotaur minotaur : currentLevel.getMinotaurs()) {
                if (skill.getHitbox().intersects(minotaur.getHitbox()) && skill.isActive()) {
                    minotaur.hurt(1);
//                    skill.setActive(false);
                } else if (IsProjectileHittingLevel1(lvlData, skill)) {
//                    skill.setActive(false);
                }
            }
            for (BringerOfDeath bringerOfDeath : currentLevel.getBringerOfDeath()) {
                if (skill.getHitbox().intersects(bringerOfDeath.getHitbox()) && skill.isActive()) {
                    bringerOfDeath.hurt(19);
                    skill.setActive(false);
                } else if (IsProjectileHittingLevel1(lvlData, skill)) {
//                    skill.setActive(false);
                }
            }
            for (Pinkstar pinkstar : currentLevel.getPinkstars()) {
                if (skill.getHitbox().intersects(pinkstar.getHitbox()) && skill.isActive()) {
                    pinkstar.hurt(1);
//                    skill.setActive(false);
                } else if (IsProjectileHittingLevel1(lvlData, skill)) {
//                    skill.setActive(false);
                }
            }
            for (Shark shark : currentLevel.getSharks()) {
                if (skill.getHitbox().intersects(shark.getHitbox()) && skill.isActive()) {
                    shark.hurt(1);
//                    skill.setActive(false);
                } else if (IsProjectileHittingLevel1(lvlData, skill)) {
//                    skill.setActive(false);
                }
            }
            for (Golem golem : currentLevel.getGolems()) {
                if (skill.getHitbox().intersects(golem.getHitbox()) && skill.isActive()) {
                    golem.hurt(19);
                    skill.setActive(false);
                } else if (IsProjectileHittingLevel1(lvlData, skill)) {
//                    skill.setActive(false);
                }
            }
            for (Wraith wraith : currentLevel.getWraiths()) {
                if (skill.getHitbox().intersects(wraith.getHitbox()) && skill.isActive()) {
                    wraith.hurt(19);
                    skill.setActive(false);
                } else if (IsProjectileHittingLevel1(lvlData, skill)) {
//                    skill.setActive(false);
                }
            }
        }
    }

    private void updateSpellsEffect(int[][] lvlData, Player player) {
        for (Wraith wraith : currentLevel.getWraiths()) {
            if (wraith.canSeePlayer(lvlData, player) && wraith.getAniIndex() == 17 && wraith.aniTick == 0) {
                wraithEf(wraith);
            }
        }
    }

    private void wraithEf(Wraith wraith) {
        int dir = 1;
        if (wraith.walkDir == LEFT)
            dir = -1;
        effects.add(new SpellsEffect((int) (wraith.getHitbox().x - 10 * Game.SCALE), (int) wraith.getHitbox().y, dir));
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon cannon : cannons) {
            if (!cannon.doAnimation)
                if (cannon.getTileY() == player.getTileY())
                    if (isPlayerInRange(cannon, player))
                        if (isPlayerInFrontOfCannon(cannon, player))
                            if (CanCannonSeePlayer(lvlData, cannon.getHitbox(), player.getHitbox(), cannon.getTileY()))
                                cannon.setAnimation(true);
            cannon.update();
            if (cannon.getAniIndex() == 4 && cannon.getAniTick() == 0)
                shootCannon(cannon);
        }
    }

    private void updateAttack(int[][] lvlData, Player player) {
        if (player.powerAttack3 && player.getAniIndex() == 11 && player.getanitick() == 0)
            AttackPower(player);

    }

    private void AttackPower(Player player) {
        int dir = 1;
        if (!player.s)
            dir = -1;
        skills.add(new Skill((int) (player.getHitbox().x - 10 * Game.SCALE), (int) player.getHitbox().y, dir));
    }

    private void shootCannon(Cannon cannon) {
        int dir = 1;
        if (cannon.getObjType() == CANNON_LEFT)
            dir = -1;
        projectiles.add(new Projectile((int) cannon.getHitbox().x, (int) cannon.getHitbox().y, dir));
    }

    private boolean isPlayerInFrontOfCannon(Cannon cannon, Player player) {
        if (cannon.getObjType() == CANNON_LEFT) {
            if (cannon.getHitbox().x > player.getHitbox().x) {
                return true;
            }
        } else {
            if (cannon.getHitbox().x < player.getHitbox().x) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlayerInRange(Cannon cannon, Player player) {
        int absValue = (int) Math.abs(player.getHitbox().x - cannon.getHitbox().x);
        return absValue <= Game.TILES_SIZE * 5;
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawSpikes(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
        drawPowerAttack(g, xLvlOffset);
        drawGrass(g, xLvlOffset);
        drawEf(g, xLvlOffset);
    }

    private void drawEf(Graphics g, int xLvlOffset) {
        for (SpellsEffect effect : effects) {
            if (effect.isActive()) {
                g.drawImage(effectImg, (int) (effect.getHitbox().x - xLvlOffset),
                        (int) (effect.getHitbox().y - 25 * Game.SCALE),
                        EFFECT_WIDTH, EFFECT_HEIGHT, null);
            }
        }
    }

    private void drawGrass(Graphics g, int xLvlOffset) {
        for (Grass grass : currentLevel.getGrass())
            g.drawImage(grassImgs[grass.getType()],
                    grass.getX() - xLvlOffset,
                    grass.getY(),
                    (int) (32 * Game.SCALE), (int) (32 * Game.SCALE), null);
    }

    public void drawBackgroundTrees(Graphics g, int xLvlOffset) {
        for (BackgroundTree bt : currentLevel.getTrees()) {
            int type = bt.getType();
            if (type == 9)
                type = 8;
            g.drawImage(treeImgs[type - 7][bt.getAniIndex()],
                    bt.getX() - xLvlOffset + GetTreeOffsetX(bt.getType()),
                    (int) (bt.getY() + GetTreeOffsetY(bt.getType())), GetTreeWidth(bt.getType()),
                    GetTreeHeight(bt.getType()), null);
        }
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile projectile : projectiles) {
            if (projectile.isActive()) {
                g.drawImage(cannonBallImg, (int) (projectile.getHitbox().x - xLvlOffset),
                        (int) projectile.getHitbox().y,
                        CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
            }
        }
    }

    private void drawPowerAttack(Graphics g, int xLvlOffset) {
        for (Skill skill : skills) {
            if (skill.isActive()) {
                g.drawImage(skillImg, (int) (skill.getHitbox().x - xLvlOffset - 5 * Game.SCALE),
                        (int) (skill.getHitbox().y - 18 * Game.SCALE),
                        POWER_WIDTH, POWER_HEIGHT, null);
            }
        }
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon cannon : cannons) {
            int x = (int) (cannon.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;
            if (cannon.getObjType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImgs[cannon.getAniIndex()],
                    x, (int) (cannon.getHitbox().y),
                    width, CANNON_HEIGHT, null);
        }
    }

    private void drawSpikes(Graphics g, int xLvlOffset) {
        for (Spike spike : spikes) {
            g.drawImage(spikeImg, (int) (spike.getHitbox().x - xLvlOffset),
                    (int) (spike.getHitbox().y - spike.getyDrawOffset()),
                    SPIKE_WIDTH, SPIKE_HEIGHT, null);
        }
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer container : containers) {
            if (container.isActive()) {
                int type = 0;
                if (container.getObjType() == BARREL)
                    type = 1;

                g.drawImage(containerImgs[type][container.getAniIndex()],
                        (int) (container.getHitbox().x - container.getxDrawOffset() - xLvlOffset),
                        (int) (container.getHitbox().y - container.getyDrawOffset()),
                        CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion potion : potions) {
            if (potion.isActive()) {
                int type = 0;
                if (potion.getObjType() == RED_POTION)
                    type = 1;

                g.drawImage(potionImgs[type][potion.getAniIndex()],
                        (int) (potion.getHitbox().x - potion.getxDrawOffset() - xLvlOffset),
                        (int) (potion.getHitbox().y - potion.getyDrawOffset()),
                        POTION_WIDTH, POTION_HEIGHT, null);
            }
        }
    }

    public void resetAllObjects() {

        loadObjects(playing.getLevelManager().getCurrentLevel());

        for (Potion potion : potions) {
            potion.reset();
        }

        for (GameContainer container : containers) {
            container.reset();
        }

        for (Cannon cannon : cannons) {
            cannon.reset();
        }
    }
}
