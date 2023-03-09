package levels;

import entities.*;
import main.Game;
import objects.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.*;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;

    private ArrayList<Crabby> crabs = new ArrayList<>();
    private ArrayList<Minotaur> minotaurs = new ArrayList<>();
    private ArrayList<Golem> golems = new ArrayList<>();
    private ArrayList<Wraith> wraiths = new ArrayList<>();
    private ArrayList<Pinkstar> pinkstars = new ArrayList<>();
    private ArrayList<Shark> sharks = new ArrayList<>();
    private ArrayList<Monster> monsters = new ArrayList<>();
    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<BringerOfDeath> deathBringers = new ArrayList<>();
    private ArrayList<Spike> spikes = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<Cannon> cannons = new ArrayList<>();
    private ArrayList<BackgroundTree> trees = new ArrayList<>();
    private ArrayList<Grass> grass = new ArrayList<>();

    private int lvlTilesWide; // max width of map
    private int maxTilesOffset; // max tiles offset
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        lvlData = new int[img.getHeight()][img.getWidth()];
        loadLevel();
        calcLvlOffset();
    }

    private void loadLevel() {
        // Looping through the image colors just once. Instead of one per
        // object/enemy/etc..
        // Removed many methods in HelpMethods class.

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }
        }
    }

    private void loadLevelData(int redValue, int x, int y) {
        if (redValue >= 50)
            lvlData[y][x] = 0;
        else
            lvlData[y][x] = redValue;
        switch (redValue) {
            case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 ->
                    grass.add(new Grass((int) (x * Game.TILES_SIZE), (int) (y * Game.TILES_SIZE) - Game.TILES_SIZE, getRndGrassType(x)));
        }
    }

    private int getRndGrassType(int xPos) {
        return xPos % 2;
    }

    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue) {
            case CRABBY -> crabs.add(new Crabby(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case PINKSTAR -> pinkstars.add(new Pinkstar(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case SHARK -> sharks.add(new Shark(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case MINOTAUR -> minotaurs.add(new Minotaur(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case MONSTER -> monsters.add(new Monster(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case BRINGER_OF_DEATH -> deathBringers.add(new BringerOfDeath(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case GOLEM -> golems.add(new Golem(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case WRAITH -> wraiths.add(new Wraith(x * Game.TILES_SIZE, y * Game.TILES_SIZE));


            case 100 -> playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case RED_POTION, BLUE_POTION ->
                    potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case BOX, BARREL -> containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case SPIKE -> spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
            case CANNON_LEFT, CANNON_RIGHT ->
                    cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case TREE_ONE, TREE_TWO, TREE_THREE ->
                    trees.add(new BackgroundTree(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
        }
    }

    private void calcLvlOffset() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getMaxLvlOffsetX() {
        return maxLvlOffsetX;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    public ArrayList<Shark> getSharks() {
        return sharks;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }


    public ArrayList<Pinkstar> getPinkstars() {
        return pinkstars;
    }

    public ArrayList<Minotaur> getMinotaurs() {
        return minotaurs;
    }
    public ArrayList<Golem> getGolems() {
        return golems;
    }
    public ArrayList<Wraith> getWraiths() {
        return wraiths;
    }



    public ArrayList<Grass> getGrass() {
        return grass;
    }

    public ArrayList<BackgroundTree> getTrees() {
        return trees;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

    public ArrayList<BringerOfDeath> getBringerOfDeath() {
        return deathBringers;
    }

}
