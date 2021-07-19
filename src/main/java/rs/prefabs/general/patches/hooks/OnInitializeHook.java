package rs.prefabs.general.patches.hooks;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import javassist.CtBehavior;
import rs.prefabs.general.interfaces.Prefabscriber;

@SpirePatch(clz = CardCrawlGame.class, method = "create")
public class OnInitializeHook {

    @SpireInsertPatch(locator = Locator.class)
    public static void InsertHook(CardCrawlGame _inst) {
        Prefabscriber.publishOnInitialize();
    }

    private static class Locator extends SpireInsertLocator {

        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher.NewExprMatcher matcher = new Matcher.NewExprMatcher(LocalizedStrings.class);
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}