package rs.prefabs.general.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import rs.prefabs.general.interfaces.Prefabscriber;
import rs.prefabs.general.listeners.UseCardListener;

import java.lang.reflect.Field;

public class OnUseCardPatches {
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class UseCardActionPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(UseCardAction _inst) throws Exception {
            Field duration = AbstractGameAction.class.getDeclaredField("duration");
            duration.setAccessible(true);
            float dur = duration.getFloat(_inst);
            if (dur == 0.15F) {
                Field targetCard = _inst.getClass().getDeclaredField("targetCard");
                targetCard.setAccessible(true);
                AbstractCard card = (AbstractCard) targetCard.get(_inst);
                Prefabscriber.publishCardPlayed(card, _inst);
                UseCardListener.onCardPlayed(card, _inst);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "isInAutoplay");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class BeforeCardUsedPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractPlayer _inst, AbstractCard c, AbstractMonster m, int energyOnUse) {
            Prefabscriber.publishPlayingCard(c, m, energyOnUse);
            UseCardListener.onPlayingCard(c, m, energyOnUse);
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractCard.class, "use");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}