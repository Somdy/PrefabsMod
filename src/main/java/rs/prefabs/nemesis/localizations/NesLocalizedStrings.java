package rs.prefabs.nemesis.localizations;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.Settings;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.localizations.PrefabCardStrings;
import rs.prefabs.nemesis.NesDebug;
import rs.prefabs.nemesis.utils.NesGeneralUtils;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NesLocalizedStrings implements NesGeneralUtils {
    private static Map<String, PrefabCardStrings> cards;
    
    public NesLocalizedStrings() {
        long startime = System.currentTimeMillis();

        Gson gson = new Gson();
        String path = "PrefabsAssets/NemesisProperties/localizations/" + getSupportedLanguage(Settings.language) + "/";
        
        String cardPath = path + "cards.json";
        Type cardType = (new TypeToken<Map<String, PrefabCardStrings>>() {}).getType();
        cards = gson.fromJson(loadJson(cardPath), cardType);
        
        
        Log("天罚本地化主要文件加载完成：" + (System.currentTimeMillis() - startime) + "ms");
    }

    public PrefabCardStrings getCardStrings(@NotNull String id) {
        if (id.equals("Unknown")) {
            return PrefabCardStrings.getMockingStrings();
        }
        if (!cards.containsKey(id)) {
            Log("Unable to find " + id + ", check if it's misspelt.");
            return PrefabCardStrings.getMockingStrings();
        }
        return cards.get(id);
    }

    private String loadJson(String path) {
        if (!Gdx.files.internal(path).exists())
            Log("Localization does not exist: " + path);
        return Gdx.files.internal(path).readString(String.valueOf(StandardCharsets.UTF_8));
    }
    
    private void Log(Object what) {
        NesDebug.Log(this, what);
    }
}