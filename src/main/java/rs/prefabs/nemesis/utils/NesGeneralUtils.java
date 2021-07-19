package rs.prefabs.nemesis.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.utils.GeneralUtils;
import rs.prefabs.nemesis.IDs.CardID;
import rs.prefabs.nemesis.cards.AbstractNesCard;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public interface NesGeneralUtils extends GeneralUtils {
    
    default int returnNesCardPrefabID(@NotNull AbstractNesCard card) {
        int ID = -1;
        String name = card.getClass().getSimpleName();
        for (Field f : CardID.IDs) {
            if (f.getName().equals(name)) {
                try {
                    return f.getInt(CardID.class);
                } catch (Exception e) {
                    return ID;
                }
            }
        }
        return ID;
    }
    
    default boolean isCardEnchantable(AbstractCard card) {
        if (card instanceof AbstractNesCard) {
            return ((AbstractNesCard) card).canEnchant();
        }
        return false;
    }
    
    default boolean isCardEnchanted(AbstractCard card) {
        if (card instanceof AbstractNesCard)
            return ((AbstractNesCard) card).isEnchanted();
        return false;
    }
    
    default void enchantCard(AbstractCard card) {
        if (!isCardEnchantable(card)) return;
        if (card instanceof AbstractNesCard) ((AbstractNesCard) card).enchant();;
    }
    
    default void setEnchantmentTrigger(AbstractCard card, Predicate<AbstractCard> trigger) {
        if (!isCardEnchantable(card)) return;
        if (card instanceof AbstractNesCard)
            ((AbstractNesCard) card).setCanTriggerEnchantment(trigger);
    }
}