package rs.prefabs.nemesis.patches.fixes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import rs.prefabs.general.utils.PrefabUtils;
import rs.prefabs.nemesis.cards.AbstractNesCard;

import java.lang.reflect.Field;

public class RenderCardOutlookSingleViewFix {
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderTitle")
    public static class RenderUpgradeAndEnchantedTitle {
        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup _inst, SpriteBatch sb) throws Exception {
            Field c = _inst.getClass().getDeclaredField("card");
            c.setAccessible(true);
            AbstractCard card = (AbstractCard) c.get(_inst);
            if (card instanceof AbstractNesCard && (card.upgraded || ((AbstractNesCard) card).isEnchanted())) {
                Color color;
                if (card.upgraded && !((AbstractNesCard) card).isEnchanted())
                    color = ((AbstractNesCard) card).getFavoriteColor();
                else if (!card.upgraded)
                    color = PrefabUtils.complement(AbstractNesCard.ENCHANTED_COLOR);
                else
                    color = Color.GOLDENROD.cpy();
                if (card.isLocked) {
                    FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, SingleCardViewPopup.TEXT[4],
                            Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F + 338.0F * Settings.scale, Settings.CREAM_COLOR);
                }
                else if (card.isSeen) {
                    FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, card.name,
                            Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F + 338.0F * Settings.scale, color);
                } else {
                    FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, SingleCardViewPopup.TEXT[5],
                            Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F + 338.0F * Settings.scale, Settings.CREAM_COLOR);
                }
            }
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardBanner")
    public static class RenderEnchantedBanner {
        @SpirePrefixPatch
        public static SpireReturn Prefix(SingleCardViewPopup _inst, SpriteBatch sb) throws Exception {
            Field card = SingleCardViewPopup.class.getDeclaredField("card");
            card.setAccessible(true);
            AbstractCard c = (AbstractCard) card.get(_inst);
            if (c instanceof AbstractNesCard && ((AbstractNesCard) c).isEnchanted()) {
                Color color = AbstractNesCard.ENCHANTED_COLOR;
                TextureAtlas.AtlasRegion banner = ((AbstractNesCard) c).getRegionFromImages("PrefabsAssets/NemesisProperties/images/cardui/1024/banner.png");
                customRenderHelper(sb, color, Settings.WIDTH / 2F, Settings.HEIGHT / 2F, banner);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame")
    public static class RenderEnchantedFrame {
        @SpirePrefixPatch
        public static SpireReturn Prefix(SingleCardViewPopup _inst, SpriteBatch sb) throws Exception {
            Field card = SingleCardViewPopup.class.getDeclaredField("card");
            card.setAccessible(true);
            AbstractCard c = (AbstractCard) card.get(_inst);
            if (c instanceof AbstractNesCard && ((AbstractNesCard) c).isEnchanted()) {
                Color color = AbstractNesCard.ENCHANTED_COLOR;
                TextureAtlas.AtlasRegion banner;
                switch (c.type) {
                    case ATTACK:
                        banner = ((AbstractNesCard) c).getRegionFromImages("PrefabsAssets/NemesisProperties/images/cardui/1024/frame_attack.png");
                        break;
                    case SKILL:
                        banner = ((AbstractNesCard) c).getRegionFromImages("PrefabsAssets/NemesisProperties/images/cardui/1024/frame_skill.png");
                        break;
                    case POWER:
                        banner = ((AbstractNesCard) c).getRegionFromImages("PrefabsAssets/NemesisProperties/images/cardui/1024/frame_power.png");
                        break;
                    default:
                        return SpireReturn.Continue();
                }
                customRenderHelper(sb, color, Settings.WIDTH / 2F, Settings.HEIGHT / 2F, banner);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    private static void customRenderHelper(SpriteBatch sb, Color color, float x, float y, TextureAtlas.AtlasRegion img) {
        if (img != null) {
            sb.setColor(color);
            sb.draw(img, x + img.offsetX - img.originalWidth / 2F, y + img.offsetY - img.originalHeight / 2F,
                    img.originalWidth / 2F - img.offsetX, img.originalHeight / 2F - img.offsetY,
                    img.packedWidth, img.packedHeight, Settings.scale, Settings.scale, 0F);
            sb.setColor(Color.WHITE.cpy());
        }
    }
}