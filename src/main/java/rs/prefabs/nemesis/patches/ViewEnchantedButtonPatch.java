package rs.prefabs.nemesis.patches;

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import javassist.CtBehavior;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.cards.AbstractNesCard;
import rs.prefabs.nemesis.character.TheNemesis;
import rs.prefabs.nemesis.utils.SwitchMgr;

import java.lang.reflect.Field;

public class ViewEnchantedButtonPatch {
    private static String tabName = null;
    private static final String EnchantText = CardCrawlGame.languagePack.getUIString(NesFab.makeID("enchantViewButton")).TEXT[0];
    
    @SpirePatch(optional = true,
            cls = "basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix$Render",
            method = "Insert")
    public static class catchTabName {
        @SpireInsertPatch(locator = TabNameLocator.class, localvars = {"tabName", "color"})
        public static void InsertCatcher(ColorTabBar _inst, SpriteBatch sb, float y, ColorTabBar.CurrentTab curTab,
                                     @ByRef String[] tN, Color color) {
            if (tN[0].equals(TheNemesis.NAME) && !tN[0].equals(tabName))
                tabName = tN[0];
        }

        private static class TabNameLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }
    
    public static void initButton(ColorTabBar _inst, ColorTabBar.CurrentTab tab) {
        if (tab == ColorTabBarFix.Enums.MOD && tabName.equals(TheNemesis.NAME)) {
            EnchantedBtnField.viewEnchantBtn.set(_inst, new Hitbox(340.0F * Settings.scale, 48.0F * Settings.scale));
        }
    }
    
    @SpirePatch(clz = ColorTabBar.class, method = "update")
    public static class updatePatch {
        @SpirePostfixPatch
        public static void postUpdate(ColorTabBar _inst, float y) {
            if (EnchantedBtnField.viewEnchantBtn.get(_inst) != null) {
                Hitbox btn = EnchantedBtnField.viewEnchantBtn.get(_inst);
                btn.move(1110F * Settings.xScale, y);
                btn.update();
                if (btn.justHovered)
                    CardCrawlGame.sound.playA("UI_HOVER", -0.3F);
                if (btn.hovered && InputHelper.justClickedLeft)
                    btn.clickStarted = true;
                if (btn.clicked || (btn.hovered && CInputActionSet.select.isJustPressed())) {
                    btn.clicked = false;
                    CardCrawlGame.sound.playA("UI_CLICK_1", -0.2F);
                    SwitchMgr.isViewingEnchanted = !SwitchMgr.isViewingEnchanted;
                }
            } else initButton(_inst, _inst.curTab);
        }
    }
    
    @SpirePatch(clz = ColorTabBar.class, method = "render")
    public static class renderPatch {
        @SpirePostfixPatch
        public static void postRender(ColorTabBar _inst, SpriteBatch sb, float y) {
            if (EnchantedBtnField.viewEnchantBtn.get(_inst) != null) {
                Hitbox btn = EnchantedBtnField.viewEnchantBtn.get(_inst);
                renderEnchantViewText(btn, sb, y);
                btn.render(sb);
            }
        }
        
        private static void renderEnchantViewText(Hitbox enchantBtn, SpriteBatch sb, float y) {
            Color c = Settings.CREAM_COLOR;
            if (enchantBtn.hovered)
                c = Settings.GOLD_COLOR;
            FontHelper.renderFontRightAligned(sb, FontHelper.topPanelInfoFont, EnchantText, 1196.0F * Settings.xScale, y, c);
            Texture img = SwitchMgr.isViewingEnchanted ? ImageMaster.COLOR_TAB_BOX_TICKED : ImageMaster.COLOR_TAB_BOX_UNTICKED;
            sb.setColor(c);
            sb.draw(img, 1182.0F * Settings.xScale - FontHelper.getSmartWidth(FontHelper.topPanelInfoFont, 
                    EnchantText, 9999.0F, 0.0F) - 24.0F, y - 24.0F, 24.0F, 24.0F, 
                    48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 
                    48, 48, false, false);
        }
    }
    
    @SpirePatch(clz = CardLibraryScreen.class, method = "open")
    public static class switchFalseOnOpen {
        @SpirePrefixPatch
        public static void Prefix(CardLibraryScreen _inst) {
            SwitchMgr.isViewingEnchanted = false;
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "close")
    public static class switchFalseOnClose {
        @SpirePrefixPatch
        public static void Prefix(SingleCardViewPopup _inst) {
            SwitchMgr.isViewingEnchanted = false;
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "render")
    public static class renderEnchantedView {
        @SpireInsertPatch(locator = Locator.class, localvars = {"copy"})
        public static void Insert(SingleCardViewPopup _inst, SpriteBatch sb, @ByRef AbstractCard[] copy) throws Exception {
            Field card = _inst.getClass().getDeclaredField("card");
            card.setAccessible(true);
            AbstractCard c = (AbstractCard) card.get(_inst);
            if (c instanceof AbstractNesCard) {
                if (SwitchMgr.isViewingEnchanted && ((AbstractNesCard) c).canEnchant()) {
                    ((AbstractNesCard) c).enchant();
                }
            }
        }
        
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(SingleCardViewPopup.class, "isViewingUpgrade");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}