package rs.prefabs.nemesis.patches.fixes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import rs.prefabs.nemesis.cards.AbstractNesCard;
import rs.prefabs.nemesis.patches.NesCardEnum;

import java.lang.reflect.Field;

public class RenderOfferingCardTypeFix {
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardTypeText")
    public static class RenderOfferingType {
        @SpirePrefixPatch
        public static SpireReturn Prefix(SingleCardViewPopup _inst, SpriteBatch sb) throws Exception {
            Field card = _inst.getClass().getDeclaredField("card");
            card.setAccessible(true);
            AbstractCard c = (AbstractCard) card.get(_inst);
            if (c.hasTag(NesCardEnum.OFFERING)) {
                FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, AbstractNesCard.OFFERING_TYPE,
                        Settings.WIDTH / 2F + 3F * Settings.scale, Settings.HEIGHT / 2F - 40F * Settings.scale, 
                        new Color(0.35F, 0.35F, 0.35F, 1.0F));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}