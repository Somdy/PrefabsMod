package rs.prefabs.nemesis.patches.fixes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import rs.prefabs.nemesis.cards.AbstractNesCard;

import java.lang.reflect.Method;

public class EnchantPreviewFix {

    public static boolean checkIfOnlyViewUpgrade(HandCardSelectScreen _inst) {
        return !EnchantPreviewField.forEnchant.get(_inst);
    }
    
    @SpirePatch(clz = HandCardSelectScreen.class, method = "selectHoveredCard")
    public static class HandSelectHoverPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("forUpgrade")) {
                        f.replace("$_ = $proceed($$) && "
                                + EnchantPreviewFix.class.getName() + ".checkIfOnlyViewUpgrade(this);");
                    }
                }
            };
        }
        
        @SpireInsertPatch(locator = Locator.class)
        public static void InsertEnchant(HandCardSelectScreen _inst) {
            if (EnchantPreviewField.forEnchant.get(_inst) && _inst.selectedCards.size() == 1) {
                AbstractCard card = _inst.selectedCards.group.get(0);
                if (card instanceof AbstractNesCard) {
                    EnchantPreviewField.enchantPreview.set(_inst, (AbstractNesCard) card.makeStatEquivalentCopy());
                    EnchantPreviewField.enchantPreview.get(_inst).enchant();
                    EnchantPreviewField.enchantPreview.get(_inst).drawScale = 0.75F;
                }
            }
        }
        
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(HandCardSelectScreen.class, "forUpgrade");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
    
    @SpirePatch(clz = HandCardSelectScreen.class, method = "updateMessage")
    public static class HandSelectUpdateMsgPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("forUpgrade")) {
                        f.replace("$_ = $proceed($$) && "
                                + EnchantPreviewFix.class.getName() + ".checkIfOnlyViewUpgrade(this);");
                    }
                }
            };
        }
        
        @SpirePrefixPatch
        public static void PrefixEnchant(HandCardSelectScreen _inst) {
            if (_inst.selectedCards.group.size() == 0 && EnchantPreviewField.enchantPreview.get(_inst) != null)
                EnchantPreviewField.enchantPreview.set(_inst, null);
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void InsertEnchant(HandCardSelectScreen _inst) {
            if (EnchantPreviewField.forEnchant.get(_inst) && _inst.selectedCards.size() == 1) {
                AbstractCard card = _inst.selectedCards.group.get(0);
                if (card instanceof AbstractNesCard) {
                    EnchantPreviewField.enchantPreview.set(_inst, (AbstractNesCard) card.makeStatEquivalentCopy());
                    EnchantPreviewField.enchantPreview.get(_inst).enchant();
                    EnchantPreviewField.enchantPreview.get(_inst).drawScale = 0.75F;
                    EnchantPreviewField.enchantPreview.get(_inst).targetDrawScale = 0.75F;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(HandCardSelectScreen.class, "forUpgrade");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(clz = HandCardSelectScreen.class, method = "render")
    public static class HandSelectRenderPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("forUpgrade")) {
                        f.replace("$_ = $proceed($$) && "
                                + EnchantPreviewFix.class.getName() + ".checkIfOnlyViewUpgrade(this);");
                    }
                }
            };
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void InsertEnchant(HandCardSelectScreen _inst, SpriteBatch sb) throws Exception {
            if (EnchantPreviewField.forEnchant.get(_inst) && EnchantPreviewField.enchantPreview.get(_inst) != null) {
                AbstractNesCard card = EnchantPreviewField.enchantPreview.get(_inst);
                Method rA = _inst.getClass().getDeclaredMethod("renderArrows", SpriteBatch.class);
                rA.setAccessible(true);
                rA.invoke(_inst, sb);
                card.current_x = Settings.WIDTH * 0.63F;
                card.current_y = Settings.HEIGHT / 2.0F + 160.0F * Settings.scale;
                card.target_x = Settings.WIDTH * 0.63F;
                card.target_y = Settings.HEIGHT / 2.0F + 160.0F * Settings.scale;
                boolean t1 = card.isDamageModified;
                boolean t2 = card.isBlockModified;
                boolean t3 = card.isMagicNumberModified;
                boolean t4 = card.isCostModified;
                boolean t5 = card.isExMagicNumModified();
                card.applyPowers();
                if (!card.isDamageModified && t1)
                    card.isDamageModified = true;
                if (!card.isBlockModified && t2)
                    card.isBlockModified = true;
                if (!card.isMagicNumberModified && t3)
                    card.isMagicNumberModified = true;
                if (!card.isCostModified && t4)
                    card.isCostModified = true;
                if (!card.isExMagicNumModified() && t5)
                    card.setExMagicNumModified(true);
                
                card.render(sb);
                card.updateHoverLogic();
                card.renderCardTip(sb);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(HandCardSelectScreen.class, "forUpgrade");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}