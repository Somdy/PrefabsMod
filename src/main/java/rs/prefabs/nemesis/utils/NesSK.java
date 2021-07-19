package rs.prefabs.nemesis.utils;

import com.badlogic.gdx.Gdx;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.utils.SK;
import rs.prefabs.nemesis.IDs.CardID;

public class NesSK extends SK {
    
    // Card utils here
    @NotNull
    public static String GetNesCardImage(String key, String id, int index) {
        StringBuilder path = new StringBuilder("PrefabsAssets/NemesisProperties/images/cardSets/");
        StringBuilder security = new StringBuilder(path);
        if (Gdx.files.internal(security.append(key).append("/").append(id).append("_").append(index).append(".png").toString()).exists())
            return path.append(key).append("/").append(id).append("_").append(index).append(".png").toString();
        //NesDebug.Log("Unable to find " + security.toString() + ", returning wildcard image.");
        return "PrefabsAssets/NemesisProperties/images/cardSets/wildcard.png";
    }
    
    // Relic utils here
    public static String GetNesRelicImage(String id) {
        StringBuilder path = new StringBuilder("PrefabsAssets/NemesisProperties/images/relics/");
        StringBuilder security = new StringBuilder(path);
        if (Gdx.files.internal(security.append(id).append(".png").toString()).exists())
            return path.append(id).append(".png").toString();
        return "PrefabsAssets/NemesisProperties/images/relics/TestRelic.png";
    }
    
    public static String GetNesRelicOutline(String id) {
        StringBuilder path = new StringBuilder("PrefabsAssets/NemesisProperties/images/relics/outline/");
        StringBuilder security = new StringBuilder(path);
        if (Gdx.files.internal(security.append(id).append(".png").toString()).exists())
            return path.append(id).append(".png").toString();
        return "PrefabsAssets/NemesisProperties/images/relics/outline/TestRelic.png";
    }
}