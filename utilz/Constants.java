package utilz;

import main.Game;

public class Constants {

    public static final float GRAVITY = 0.04f * Game.SCALE;
    public static int ANI_SPEED = 20;

    public static class Projectiles {
        public static final int CANNON_BALL_WIDTH_DEFAULT = 15;
        public static final int CANNON_BALL_HEIGHT_DEFAULT = 15;
        public static final int CANNON_BALL_WIDTH = (int) (Game.SCALE * CANNON_BALL_WIDTH_DEFAULT);
        public static final int CANNON_BALL_HEIGHT = (int) (Game.SCALE * CANNON_BALL_HEIGHT_DEFAULT);
        public static final float SPEED = 0.75f * Game.SCALE;

        public static final int POWER_WIDTH_DEFAULT = 111;
        public static final int POWER_HEIGHT_DEFAULT = 44;
        public static final int POWER_WIDTH = (int) (Game.SCALE * POWER_WIDTH_DEFAULT);
        public static final int POWER_HEIGHT = (int) (Game.SCALE * POWER_HEIGHT_DEFAULT);

        public static final int EFFECT_WIDTH_DEFAULT = 64;
        public static final int EFFECT_HEIGHT_DEFAULT = 64;
        public static final int EFFECT_WIDTH = (int) (Game.SCALE * EFFECT_WIDTH_DEFAULT);
        public static final int EFFECT_HEIGHT = (int) (Game.SCALE * EFFECT_HEIGHT_DEFAULT);
    }

    public static class ObjectConstants {
        // Type of objects
        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BARREL = 2;
        public static final int BOX = 3;
        public static final int SPIKE = 4;
        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;
        public static final int TREE_ONE = 7;
        public static final int TREE_TWO = 8;
        public static final int TREE_THREE = 9;


        // Regen amount
        public static final int RED_POTION_VALUE = 5;
        public static final int BLUE_POTION_VALUE = 10;

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (Game.SCALE * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT = (int) (Game.SCALE * CONTAINER_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (Game.SCALE * POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT = (int) (Game.SCALE * POTION_HEIGHT_DEFAULT);

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (Game.SCALE * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int) (Game.SCALE * SPIKE_HEIGHT_DEFAULT);

        public static final int CANNON_WIDTH_DEFAULT = 40;
        public static final int CANNON_HEIGHT_DEFAULT = 26;
        public static final int CANNON_WIDTH = (int) (Game.SCALE * CANNON_WIDTH_DEFAULT);
        public static final int CANNON_HEIGHT = (int) (Game.SCALE * CANNON_HEIGHT_DEFAULT);

        public static int GetSpriteAmount(int objType) {
            switch (objType) {
                case RED_POTION, BLUE_POTION:
                    return 7;
                case BOX, BARREL:
                    return 8;
                case CANNON_LEFT, CANNON_RIGHT:
                    return 7;
            }
            return 1;
        }

        public static int GetTreeOffsetX(int treeType) {
            switch (treeType) {
                case TREE_ONE:
                    return (Game.TILES_SIZE / 2) - (GetTreeWidth(treeType) / 2);
                case TREE_TWO:
                    return (int) (Game.TILES_SIZE / 2.5f);
                case TREE_THREE:
                    return (int) (Game.TILES_SIZE / 1.65f);
            }
            return 0;
        }

        public static int GetTreeOffsetY(int treeType) {
            switch (treeType) {
                case TREE_ONE:
                    return -GetTreeHeight(treeType) + Game.TILES_SIZE * 2;
                case TREE_TWO, TREE_THREE:
                    return -GetTreeHeight(treeType) + (int) (Game.TILES_SIZE / 1.25f);
            }
            return 0;
        }

        public static int GetTreeWidth(int treeType) {
            switch (treeType) {
                case TREE_ONE:
                    return (int) (39 * Game.SCALE);
                case TREE_TWO:
                    return (int) (62 * Game.SCALE);
                case TREE_THREE:
                    return -(int) (62 * Game.SCALE);
            }
            return 0;
        }

        public static int GetTreeHeight(int treeType) {
            switch (treeType) {
                case TREE_ONE:
                    return (int) (92 * Game.SCALE);
                case TREE_TWO, TREE_THREE:
                    return (int) (54 * Game.SCALE);
            }
            return 0;
        }
    }


    public static class EnemyConstants {
        public static final int CRABBY = 0;
        public static final int PINKSTAR = 1;
        public static final int SHARK = 2;
        public static final int MINOTAUR = 3;
        public static final int MONSTER = 4;
        public static final int BRINGER_OF_DEATH = 5;
        public static final int GOLEM = 6;
        public static final int WRAITH = 7;


        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int GOLEM_WIDTH_DEFAULT = 75;
        public static final int GOLEM_HEIGHT_DEFAULT = 75;
        public static final int GOLEM_WIDTH = (int) (GOLEM_WIDTH_DEFAULT * Game.SCALE);
        public static final int GOLEM_HEIGHT = (int) (GOLEM_HEIGHT_DEFAULT * Game.SCALE);
        public static final int GOLEM_DRAWOFFSET_X = (int) (3 * Game.SCALE);
        public static final int GOLEM_DRAWOFFSET_Y = (int) (31 * Game.SCALE);

        public static final int WRAITH_WIDTH_DEFAULT = 75;
        public static final int WRAITH_HEIGHT_DEFAULT = 60;
        public static final int WRAITH_WIDTH = (int) (WRAITH_WIDTH_DEFAULT * Game.SCALE);
        public static final int WRAITH_HEIGHT = (int) (WRAITH_HEIGHT_DEFAULT * Game.SCALE);
        public static final int WRAITH_DRAWOFFSET_X = (int) (3 * Game.SCALE);
        public static final int WRAITH_DRAWOFFSET_Y = (int) (28 * Game.SCALE);
        public static final int BRINGER_OF_DEATH_WIDTH_DEFAULT = 146;
        public static final int BRINGER_OF_DEATH_HEIGHT_DEFAULT = 98;
        public static final int BRINGER_OF_DEATH_WIDTH = (int) (BRINGER_OF_DEATH_WIDTH_DEFAULT * Game.SCALE);
        public static final int BRINGER_OF_DEATH_HEIGHT = (int) (BRINGER_OF_DEATH_HEIGHT_DEFAULT * Game.SCALE);
        public static final int BRINGER_OF_DEATH_DRAWOFFSET_X = (int) (20 * Game.SCALE);
        public static final int BRINGER_OF_DEATH_DRAWOFFSET_Y = (int) (35 * Game.SCALE);
        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;
        public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * Game.SCALE);
        public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * Game.SCALE);
        public static final int CRABBY_DRAWOFFSET_X = (int) (26 * Game.SCALE);
        public static final int CRABBY_DRAWOFFSET_Y = (int) (9 * Game.SCALE);

        public static final int PINKSTAR_WIDTH_DEFAULT = 34;
        public static final int PINKSTAR_HEIGHT_DEFAULT = 30;
        public static final int PINKSTAR_WIDTH = (int) (PINKSTAR_WIDTH_DEFAULT * Game.SCALE);
        public static final int PINKSTAR_HEIGHT = (int) (PINKSTAR_HEIGHT_DEFAULT * Game.SCALE);
        public static final int PINKSTAR_DRAWOFFSET_X = (int) (9 * Game.SCALE);
        public static final int PINKSTAR_DRAWOFFSET_Y = (int) (7 * Game.SCALE);

        public static final int SHARK_WIDTH_DEFAULT = 34;
        public static final int SHARK_HEIGHT_DEFAULT = 30;
        public static final int SHARK_WIDTH = (int) (SHARK_WIDTH_DEFAULT * Game.SCALE);
        public static final int SHARK_HEIGHT = (int) (SHARK_HEIGHT_DEFAULT * Game.SCALE);
        public static final int SHARK_DRAWOFFSET_X = (int) (8 * Game.SCALE);
        public static final int SHARK_DRAWOFFSET_Y = (int) (6 * Game.SCALE);

        public static final int MINOTAUR_WIDTH_DEFAULT = 64;
        public static final int MINOTAUR_HEIGHT_DEFAULT = 55;

        public static final int MINOTAUR_WIDTH = (int) (MINOTAUR_WIDTH_DEFAULT * Game.SCALE);
        public static final int MINOTAUR_HEIGHT = (int) (MINOTAUR_HEIGHT_DEFAULT * Game.SCALE);

        public static final int MINOTAUR_DRAWOFFSET_X = (int) (1 * Game.SCALE);
        public static final int MINOTAUR_DRAWOFFSET_Y = (int) (4 * Game.SCALE);
        public static final int MONSTER_WIDTH_DEFAULT = 160;
        public static final int MONSTER_HEIGHT_DEFAULT = 190;

        public static final int MONSTER_WIDTH = (int) (MONSTER_WIDTH_DEFAULT * Game.SCALE);
        public static final int MONSTER_HEIGHT = (int) (MONSTER_HEIGHT_DEFAULT * Game.SCALE);

        public static final int MONSTER_DRAWOFFSET_X = (int) (1 * Game.SCALE);
        public static final int MONSTER_DRAWOFFSET_Y = (int) (10 * Game.SCALE);

        public static int GetSpriteAmount(int enemyType, int enemyState) {
            switch (enemyState) {
                case IDLE: {
                    if (enemyType == CRABBY)
                        return 9;
                    else if (enemyType == SHARK || enemyType == PINKSTAR || enemyType == MONSTER || enemyType == BRINGER_OF_DEATH)
                        return 8;
                    else if (enemyType == MINOTAUR || enemyType == WRAITH)
                        return 12;
                    else if (enemyType == GOLEM) return 18;
                }
                case RUNNING:
                    if (enemyType == MINOTAUR)
                        return 18;
                    else if (enemyType == BRINGER_OF_DEATH)
                        return 8;
                    else if (enemyType == GOLEM || enemyType == WRAITH) return 12;
                    return 6;
                case ATTACK:
                    if (enemyType == SHARK)
                        return 8;
                    else if (enemyType == MINOTAUR || enemyType == GOLEM)
                        return 12;
                    else if (enemyType == BRINGER_OF_DEATH) return 10;
                    else if (enemyType == WRAITH) return 18;
                    return 7;
                case HIT:
                    if (enemyType == MINOTAUR || enemyType == GOLEM || enemyType == WRAITH)
                        return 12;
                    else if (enemyType == MONSTER) return 2;
                    else if (enemyType == BRINGER_OF_DEATH) return 3;
                    return 4;
                case DEAD:
                    if (enemyType == MINOTAUR)
                        return 13;
                    else if (enemyType == MONSTER || enemyType == BRINGER_OF_DEATH || enemyType == WRAITH) return 10;
                    else if (enemyType == GOLEM) return 15;
                    return 5;
            }
            return 0;
        }

        public static int GetMaxHealth(int enemyType) {
            switch (enemyType) {
                case CRABBY:
                    return 10;
                case PINKSTAR, SHARK:
                    return 10;
                case MINOTAUR:
                    return 15;
                case GOLEM:
                    return 40;
                case WRAITH:
                    return 43;
                case BRINGER_OF_DEATH:
                    return 45;
                case MONSTER:
                    return 50;
                default:
                    return 1;
            }
        }

        public static int GetEnemyDmg(int enemyType) {
            switch (enemyType) {
                case CRABBY:
                    return 1;
                case SHARK, PINKSTAR:
                    return 2;
                case MINOTAUR:
                    return 3;
                case GOLEM:
                    return 3;
                case BRINGER_OF_DEATH, WRAITH:
                    return 6;
                case MONSTER:
                    return 9;
                default:
                    return 0;
            }
        }
    }

    public static class UI {
        public static class Button {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42; // width = height = 42
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class URMButtons {
            public static final int URM_SIZE_DEFAULT = 56;
            public static final int URM_SIZE = (int) (56 * Game.SCALE);
        }

        public static class VolumeButton {
            public static final int VOLUME_WIDTH_DEFAULT = 28;
            public static final int VOLUME_HEIGHT_DEFAULT = 44;
            public static final int SLIDER_WIDTH_DEFAULT = 215;
            public static final int VOLUME_WIDTH = (int) (VOLUME_WIDTH_DEFAULT * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_HEIGHT_DEFAULT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_WIDTH_DEFAULT * Game.SCALE);
        }

        public static class Environment {
            public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
            public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
            public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
            public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);

            public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
            public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
            public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
            public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);

        }
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;

    }

    public static class PlayerConstants {

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK_1 = 2;
        public static final int JUMP = 3;
        public static final int PowerAttack_1 = 4;
        public static final int HIT = 5;
        public static final int DEAD = 6;
        public static final int PowerAttack_2 = 7;
        public static final int PowerAttack_3 = 8;
        public static final int ATTACK_2 = 9;
        public static final int IDLE_2 = 10;


        public static int GetSpriteAmount(int player_action) { // uppercase 'G' for static method

            switch (player_action) {
                case DEAD:
                    return 3;
                case RUNNING:
                    return 8;
                case IDLE:
                    return 6;
                case HIT:
                    return 3;
                case JUMP:
                    return 1;
                case ATTACK_1:
                    return 3;
                case ATTACK_2:
                    return 9;
                case PowerAttack_1:
                    return 8;
                case PowerAttack_2:
                    return 8;
                case PowerAttack_3:
                    return 12;
                case IDLE_2:
                    return 9;
                default:
                    return 1;
            }
        }
    }
}
