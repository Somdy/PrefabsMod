package rs.prefabs.general.data;

import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.prefabs.general.interfaces.Prefabscriber;

import java.util.HashMap;
import java.util.Map;

public class CustomRarity {
    private static Map<String, AbstractCard.CardRarity> customs;
    
    public static void initialize() {
        customs = new HashMap<>();
    }
    
    public static boolean registerRarity(String key, AbstractCard.CardRarity rarity) {
        if (customs.containsKey(key))
            return false;
        customs.put(key, rarity);
        return true;
    }
    
    public static AbstractCard.CardRarity getCustomRarity(String key) {
        if (customs.containsKey(key))
            return customs.get(key);
        return null;
    }
}