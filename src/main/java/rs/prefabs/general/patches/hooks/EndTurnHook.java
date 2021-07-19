package rs.prefabs.general.patches.hooks;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.PrefabMgr;
import rs.prefabs.general.interfaces.Prefabscriber;

public class EndTurnHook {
    @SpirePatch(clz = AbstractCreature.class, method = "applyEndOfTurnTriggers")
    public static class PostEndTurnHook {
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature _inst) {
            if (_inst instanceof AbstractMonster)
                Prefabscriber.publishMonsterTurnEnds((AbstractMonster) _inst);
            else if (_inst instanceof AbstractPlayer)
                PrefabMgr.receiveOnPlayerEndsTurn((AbstractPlayer) _inst);
        }
    }

    @SpirePatch(clz = DiscardAtEndOfTurnAction.class, method = "update")
    public static class EndTurnPreDiscardHook {
        @SpireInsertPatch(rloc = 3)
        public static void Insert(DiscardAtEndOfTurnAction _inst) {
            Prefabscriber.publishEndTurnPreDiscard();
        }
    }
}