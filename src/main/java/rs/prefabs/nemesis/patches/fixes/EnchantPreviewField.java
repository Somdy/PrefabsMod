package rs.prefabs.nemesis.patches.fixes;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import rs.prefabs.nemesis.cards.AbstractNesCard;

@SpirePatch(clz = HandCardSelectScreen.class, method = SpirePatch.CLASS)
public class EnchantPreviewField {
    public static SpireField<Boolean> forEnchant = new SpireField<>(() -> false);
    public static SpireField<AbstractNesCard> enchantPreview = new SpireField<>(() -> null);
}