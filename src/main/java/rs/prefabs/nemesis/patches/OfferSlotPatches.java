package rs.prefabs.nemesis.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import rs.prefabs.nemesis.character.OfferHelper;

public class OfferSlotPatches {
    @SpirePatch(clz = EnergyPanel.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class RenderPatch {
        @SpirePrefixPatch
        public static void Prefix(EnergyPanel _inst, SpriteBatch sb) {
            OfferHelper.render(sb);
        }
    }
    @SpirePatch(clz = EnergyPanel.class, method = "update")
    public static class UpdatePatch {
        @SpirePrefixPatch
        public static void Prefix(EnergyPanel _inst) {
            OfferHelper.update();
        }
    }
}