package rs.prefabs.general.data;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.HashMap;
import java.util.Map;

public class CustomCardTags {
    private static Map<String, AbstractCard.CardTags> customs;

    public static void initialize() {
        customs = new HashMap<>();
    }

    public static boolean registerTag(String key, AbstractCard.CardTags tag) {
        if (customs.containsKey(key))
            return false;
        customs.put(key, tag);
        return true;
    }

    public static AbstractCard.CardTags getCustomTag(String tag) {
        if (customs.containsKey(tag))
            return customs.get(tag);
        return null;
    }
}