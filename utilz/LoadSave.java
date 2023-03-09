package utilz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites_new.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String MENU_BUTTON = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";
    public static final String PLAYING_BG_IMG = "playing_bg_img.png";
    public static final String PLAYING_BG_IMG1 = "playing_bg_img1.png";
    public static final String PLAYING_BG_IMG2 = "playing_bg_img2.png";
    public static final String PLAYING_BG_IMG3 = "playing_bg_img3.png";
    public static final String PLAYING_BG_IMG4 = "playing_bg_img4.png";

//    public static final String BIG_CLOUDS = "big_clouds.png";
//    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";
    public static final String DEATH_SCREEN = "death_screen.png";
    public static final String OPTION_MENU = "options_background.png";
    public static final String GAME_COMPLETED = "game_completed.png";
    public static final String WATER_TOP = "water_atlas_animation.png";
    public static final String WATER_BOTTOM = "water.png";
    public static final String TREE_ONE_ATLAS = "tree_one_atlas.png";
    public static final String TREE_TWO_ATLAS = "tree_two_atlas.png";
    public static final String GRASS_ATLAS = "grass_atlas.png";

    // Object
    public static final String POTION_ATLAS = "potions_sprites.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    public static final String CANNON_BALL = "ball.png";
    public static final String EFFECT = "Spells-Effect.png";

    // Enemy
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String SHARK_ATLAS = "shark_atlas.png";
    public static final String PINKSTAR_ATLAS = "pinkstar_atlas.png";
    public static final String MINOTAUR_ATLAS = "minotaur.png";
    public static final String MONSTER_ATLAS = "monster.png";
    public static final String BRINGER_OF_DEATH_ATLAS = "bringer_of_death_atlas.png";
    public static final String SKILL = "skill.png";
    public static final String GOLEM = "golem.png";
    public static final String WRAITH = "wraith.png";

    public static BufferedImage GetSpriteAtlas(String filename) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/res/" + filename);

        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/res/lvls");
        File file = null;

        try {
            file = new File(url.toURI()); // url is the location, uri is the resource (folder)
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File[] files = file.listFiles(); // go over all the files, and list them into 'files'
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals("" + (i + 1) + ".png")) {
                    filesSorted[i] = files[j];
                    break;
                }
            }
        }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];
        for (int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return imgs;
    }
}
