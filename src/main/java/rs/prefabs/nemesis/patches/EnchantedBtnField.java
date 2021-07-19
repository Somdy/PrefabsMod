package rs.prefabs.nemesis.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;

@SpirePatch(clz = ColorTabBar.class, method = SpirePatch.CLASS)
public class EnchantedBtnField {
    public static SpireField<Hitbox> viewEnchantBtn = new SpireField<>(() -> null);
}
