package gamestates;

import entities.Enemy;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameCompletedOverlay;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utilz.Constants.UI.Environment.*;

public class Playing extends State implements Statemethods {

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private ObjectManager objectManager;
    private boolean paused = false; // show pause screen or not

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH); // if player is 20% to the left, move the map left
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH); // if player is 20% to the right, move the map to right
    private int maxLvlOffsetX;

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPos;
    private Random rnd = new Random();

    private boolean gameOver;
    private boolean lvlCompleted;
    private boolean gameCompleted = false;
    private boolean playerDying;
    private int i = 0;
    public Playing(Game game) {
        super(game);
        initClasses();
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG1);
//        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
//        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++) {
            smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));
        }

        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        i++;
        if (i == 1) backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG2);
        if (i == 2) backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG3);
        if (i == 3) backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG4);

        levelManager.setLevelIndex(levelManager.getLvlIndex() + 1);
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        resetAll();
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
        resetAll();
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getMaxLvlOffsetX();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 200, (int) (64 * game.SCALE), (int) (40 * game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if (gameCompleted) {
            gameCompletedOverlay.update();
        } else if (gameOver) {
            Enemy.count = 0;
            gameOverOverlay.update();
            Player.k=0;
        } else if (playerDying) {
            player.update();
        } else {
            levelManager.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if (xLvlOffset > maxLvlOffsetX) // stop moving when reach max or min
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        drawClouds(g);

        levelManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        objectManager.drawBackgroundTrees(g, xLvlOffset);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (lvlCompleted) {
            levelCompletedOverlay.draw(g);
        } else if (gameCompleted) {
            gameCompletedOverlay.draw(g);
        }
    }

    private void drawClouds(Graphics g) {

        for (int i = 0; i < 3; i++) {
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }

        for (int i = 0; i < smallCloudsPos.length; i++) {
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }
    }

    public void resetGameCompleted() {
        gameCompleted = false;
    }

    public void resetAll() {
        // TODO: reset playing, enemy, lvl, ...
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public void checkSpikesTouched(Player player) {
        objectManager.checkSpikeTouched(player);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1)
                player.normalAttack();
            else if (e.getButton() == MouseEvent.BUTTON3)
                player.powerAttack();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver){

            gameOverOverlay.mousePressed(e);
        }
        else if (paused)
            pauseOverlay.mousePressed(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mousePressed(e);
        else if (gameCompleted)
            gameCompletedOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver) {
            gameOverOverlay.mouseReleased(e);

        }
        else if (paused)
            pauseOverlay.mouseReleased(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mouseReleased(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver){
            gameOverOverlay.mouseMoved(e);
        }

        else if (paused)
            pauseOverlay.mouseMoved(e);
        else if (lvlCompleted)
            levelCompletedOverlay.mouseMoved(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver){
            gameOverOverlay.keyPressed(e);

        }

        else
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A: // left
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D: // right
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE: // Jump
                    player.setJump(true);
                    break;
                case KeyEvent.VK_J: // Jump
                    player.normalAttack();
                    break;
                case KeyEvent.VK_K: // Jump
                    player.powerAttack();
                    break;
                case KeyEvent.VK_U: // Jump
                    player.powerAttack_2();
                    break;
                case KeyEvent.VK_I:  // Jump
                    player.powerAttack_3();
                    break;
                case KeyEvent.VK_ESCAPE: // open pause menu
                    paused = !paused;
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A: // left
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D: // right
                    player.setRight(false);
                    break;
                case KeyEvent.VK_J: // Jump
                    player.normalAttack();
                    break;
                case KeyEvent.VK_K: // Jump
                    player.powerAttack();
                    break;
                case KeyEvent.VK_U: // Jump
                    player.powerAttack_2();
                    break;
                case KeyEvent.VK_I: // Jump
                    player.powerAttack_3();
                    break;
                case KeyEvent.VK_SPACE: // Jump
                    player.setJump(false);
                    break;
            }
        }
    }

    public void setMaxLvlOffset(int maxLvlOffset) {
        this.maxLvlOffsetX = maxLvlOffset;
    }

    public void setLevelCompleted(boolean lvlCompleted) {
        game.getAudioPlayer().lvlCompleted();
        if (levelManager.getLvlIndex() + 1 >= levelManager.getAmountOfLevels()) {
            // No more levels
            gameCompleted = true;
            levelManager.setLevelIndex(0);
            levelManager.loadNextLevel();
            resetAll();
            return;
        }
        this.lvlCompleted = lvlCompleted;
    }

    public void unpauseGame() {
        paused = false;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }
}
