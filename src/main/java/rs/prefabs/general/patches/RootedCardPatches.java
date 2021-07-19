package rs.prefabs.general.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.events.shrines.FountainOfCurseRemoval;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rs.prefabs.general.cards.PrefabCardTags;
import rs.prefabs.general.utils.SK;

import java.util.ArrayList;

public class RootedCardPatches {
    @SpirePatch(clz = CardGroup.class, method = "getPurgeableCards")
    public static class GetPurgeablesFix {
        @SpireInsertPatch(rloc = 7, localvars = {"retVal"})
        public static void removeRootedsInsert(CardGroup _inst, @ByRef CardGroup[] retVal) {
            retVal[0].group.removeIf(c -> c.hasTag(PrefabCardTags.ROOTED) || c.hasTag(PrefabCardTags.DEEP_ROOTED));
        }
    }
    
    @SpirePatch(clz = FountainOfCurseRemoval.class, method = "buttonEffect")
    public static class FountainIgnoresRootes {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if ((m.getClassName().equals(ArrayList.class.getName()) && m.getMethodName().equals("add"))
                            || (m.getClassName().equals(CardGroup.class.getName()) && m.getMethodName().equals("removeCard"))) {
                        m.replace("if (!" + RootedCardPatches.class.getName() + ".isCardRooted(i)) " +
                                " { $_ = $proceed($$); }");
                    }
                }
            };
        }
    }
    
    public static boolean isCardRooted(int index) {
        return SK.GetPlayer().masterDeck.group.get(index).hasTag(PrefabCardTags.ROOTED)
                || SK.GetPlayer().masterDeck.group.get(index).hasTag(PrefabCardTags.DEEP_ROOTED);
    }
}