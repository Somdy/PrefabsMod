package rs.prefabs.nemesis.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import rs.prefabs.nemesis.actions.common.FranticCardAutoplayAction;
import rs.prefabs.nemesis.interfaces.FranticItem;
import rs.prefabs.nemesis.utils.FranticActuary;

@SpirePatch(clz = AbstractPlayer.class, method = "draw", paramtypez = {int.class})
public class FranticAutoplayPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
    public static void Insert(AbstractPlayer _inst, int num, AbstractCard c) {
        if (c instanceof FranticItem) {
            if (FranticActuary.canAutoPlay((FranticItem) c))
                AbstractDungeon.actionManager.addToBottom(new FranticCardAutoplayAction(c, AbstractDungeon.player.hand));
        }
    }
    
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(CardGroup.class, "addToHand");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}