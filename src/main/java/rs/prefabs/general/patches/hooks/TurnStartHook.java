package rs.prefabs.general.patches.hooks;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.PrefabMgr;
import rs.prefabs.general.interfaces.Prefabscriber;

public class TurnStartHook {
    @SpirePatch(clz = AbstractCreature.class, method = "applyStartOfTurnPostDrawPowers")
    public static class TurnStartPostDraw {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature _inst) {
            PrefabMgr.receiveOnTurnStart(_inst, true);
            Prefabscriber.publishPostTurnStart(_inst, true);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "applyStartOfTurnPowers")
    public static class TurnStart {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature _inst) {
            PrefabMgr.receiveOnTurnStart(_inst, false);
            Prefabscriber.publishPostTurnStart(_inst, false);
        }
    }
}