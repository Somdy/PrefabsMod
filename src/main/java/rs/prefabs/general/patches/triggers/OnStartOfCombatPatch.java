package rs.prefabs.general.patches.triggers;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import rs.prefabs.general.PrefabMgr;

@SpirePatch(clz = AbstractRoom.class, method = "update")
public class OnStartOfCombatPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(AbstractRoom _inst) {
        PrefabMgr.receiveOnStartOfCombat(_inst);
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior methodToPatch) throws Exception {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, 
                    "applyStartOfCombatPreDrawLogic");
            return LineFinder.findInOrder(methodToPatch, methodCallMatcher);
        }
    }
}