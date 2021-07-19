package rs.prefabs.general.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import rs.prefabs.general.listeners.UseCardListener;
import rs.prefabs.general.utils.Debugger;

import java.util.ArrayList;

public class UseCardPatches {
    @SpirePatch(clz = AbstractCard.class, method = "canUse")
    public static class CustomCanUseCardLogicPatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(AbstractCard _inst, AbstractPlayer p, AbstractMonster m) {
            if (UseCardListener.containsUnplayableCard(_inst)) {
                boolean canPlay = UseCardListener.canCardPlay(_inst, p, m);
                if (!canPlay)
                    return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }
    
    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class CopyCardsPlayedInSingleTurn {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GameActionManager _inst) {
            boolean success = UseCardListener.copyCardsPlayedLastTurn(_inst.cardsPlayedThisTurn);
            if (!success)
                Debugger.Log("Failed to copy cards play last turn");
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "clear");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}