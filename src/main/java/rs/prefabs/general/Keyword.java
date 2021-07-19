package rs.prefabs.general;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.helpers.GameDictionary;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Keyword {
    public String PROPER_NAME;
    public String[] NAMES;
    public String DESCRIPTION;
    
    public static Map<String, Keyword> fromJson(String path) {
        String keywordStrings = Gdx.files.internal(path).readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(keywordStrings, typeToken);
    }
    
    @SpirePatch(clz = GameDictionary.class, method = "initialize")
    public static class EditCustomKeywords {
        @SpirePrefixPatch
        public static void Prefix() {
            PrefabMgr.loadGeneralKeywords();
        }
    }
}