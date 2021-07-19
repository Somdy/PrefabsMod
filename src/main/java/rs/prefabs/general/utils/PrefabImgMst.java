package rs.prefabs.general.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PrefabImgMst {
    private static final TextureAtlas[] atlas;
    
    static {
        atlas = new TextureAtlas[] {
                new TextureAtlas(Gdx.files.internal("PrefabsAssets/NemesisProperties/images/powers/powers.atlas"))
        };
    }
    
    public static TextureAtlas getNesPowerAtlas() {
        return atlas[0];
    }
}