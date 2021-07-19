package rs.prefabs.nemesis.ui.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;

public class NemesisEnergyOrb implements EnergyOrbInterface {
    public static Texture NEME_ENER_LAYER1;
    public static Texture NEME_ENER_LAYER1D;
    public static Texture NEME_ENER_LAYER2;
    public static Texture NEME_ENER_LAYER2D;
    public static Texture NEME_ENER_LAYER3;
    public static Texture NEME_ENER_LAYER3D;
    public static Texture NEME_ENER_LAYER4;
    public static Texture NEME_ENER_LAYER4D;
    public static Texture NEME_ENER_LAYER5;
    public static Texture NEME_ENER_LAYER5D;
    public static Texture NEME_ENER_LAYER6;

    private static final int ORB_W = 128;
    private static final float ORB_IMG_SCALE = 1.15F * Settings.scale;
    private float angle2;
    private float angle3;
    private float angle4;
    
    public String state;

    public NemesisEnergyOrb() {
        NEME_ENER_LAYER1 = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/orbs/general/layer1.png");
        NEME_ENER_LAYER1D = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/orbs/general/layer1d.png");
        NEME_ENER_LAYER2 = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/orbs/general/layer2.png");
        NEME_ENER_LAYER2D = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/orbs/general/layer2d.png");
        NEME_ENER_LAYER3 = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/orbs/general/layer3.png");
        NEME_ENER_LAYER3D = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/orbs/general/layer3d.png");
        NEME_ENER_LAYER4 = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/orbs/general/layer4.png");
        NEME_ENER_LAYER4D = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/orbs/general/layer4d.png");
        NEME_ENER_LAYER5 = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/orbs/general/layer5.png");
        NEME_ENER_LAYER5D = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/orbs/general/layer5d.png");
        NEME_ENER_LAYER6 = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/orbs/general/layer6.png");
        
        state = "general";
    }

    @Override
    public void updateOrb(int count) {
        if (count == 0) {
            this.angle4 += Gdx.graphics.getDeltaTime() * 5.0F;
            this.angle3 += Gdx.graphics.getDeltaTime() * -8.0F;
            this.angle2 += Gdx.graphics.getDeltaTime() * 8.0F;
        } else {
            this.angle4 += Gdx.graphics.getDeltaTime() * 20.0F;
            this.angle3 += Gdx.graphics.getDeltaTime() * -40.0F;
            this.angle2 += Gdx.graphics.getDeltaTime() * 40.0F;
        }
    }

    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        if (enabled) {
            sb.setColor(Color.WHITE);

            sb.draw(NEME_ENER_LAYER2, current_x - 64.0F, current_y - 68.0F, 64.0F, 68.0F,
                    ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F,
                    0, 0, ORB_W, ORB_W, false, false);

            sb.draw(NEME_ENER_LAYER3, current_x - 64.0F, current_y - 70.0F, 64.0F, 70.0F,
                    ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F,
                    0, 0, ORB_W, ORB_W, false, false);

            sb.draw(NEME_ENER_LAYER4, current_x - 64.0F, current_y - 70.0F, 64.0F, 70.0F,
                    ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, angle3,
                    0, 0, ORB_W, ORB_W, false, false);

            sb.draw(NEME_ENER_LAYER5, current_x - 64.0F, current_y - 70.0F, 64.0F, 70.0F,
                    ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F,
                    0, 0, ORB_W, ORB_W, false, false);

            sb.setBlendFunction(770, 1);
            sb.setColor(Settings.HALF_TRANSPARENT_WHITE_COLOR);
            sb.draw(NEME_ENER_LAYER1, current_x - 64.0F, current_y - 70.0F, 64.0F, 70.0F,
                    ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, angle4,
                    0, 0, ORB_W, ORB_W, false, false);

            sb.setBlendFunction(770, 771);
            sb.setColor(Color.WHITE);
            sb.draw(NEME_ENER_LAYER6, current_x - 128.0F, current_y - 140.0F, 128.0F, 140.0F,
                    256.0F, 256.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F,
                    0, 0, 256, 256, false, false);
        }
        else {
            sb.setColor(Color.WHITE);
            sb.draw(NEME_ENER_LAYER2D, current_x - 64.0F, current_y - 68.0F, 64.0F, 68.0F,
                    ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, angle2,
                    0, 0, ORB_W, ORB_W, false, false);

            sb.draw(NEME_ENER_LAYER3D, current_x - 64.0F, current_y - 70.0F, 64.0F, 70.0F,
                    ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F,
                    0, 0, ORB_W, ORB_W, false, false);

            sb.draw(NEME_ENER_LAYER4D, current_x - 64.0F, current_y - 70.0F, 64.0F, 70.0F,
                    ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, angle3,
                    0, 0, ORB_W, ORB_W, false, false);

            sb.draw(NEME_ENER_LAYER5D, current_x - 64.0F, current_y - 70.0F, 64.0F, 70.0F,
                    ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F,
                    0, 0, ORB_W, ORB_W, false, false);

            sb.draw(NEME_ENER_LAYER1D, current_x - 64.0F, current_y - 70.0F, 64.0F, 70.0F,
                    ORB_W, ORB_W, ORB_IMG_SCALE, ORB_IMG_SCALE, angle4,
                    0, 0, ORB_W, ORB_W, false, false);

            sb.draw(NEME_ENER_LAYER6, current_x - 128.0F, current_y - 140.0F, 128.0F, 140.0F,
                    256.0F, 256.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F,
                    0, 0, 256, 256, false, false);
        }
    }
}
