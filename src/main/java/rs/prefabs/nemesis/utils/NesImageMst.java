package rs.prefabs.nemesis.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class NesImageMst {
    private static TextureAtlas vfx;
    
    public static TextureAtlas.AtlasRegion NES_PLAGUE_EFFECT;
    public static TextureAtlas.AtlasRegion NES_DECAY_EFFECT;
    
    public static void initialize() {
        vfx = new TextureAtlas(Gdx.files.internal("PrefabsAssets/NemesisProperties/images/vfx/vfx.atlas"));
        NES_PLAGUE_EFFECT = vfx.findRegion("combat/plague");
        NES_DECAY_EFFECT = vfx.findRegion("combat/decay");
    }
}