package rs.prefabs.general.patches.triggers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.prefabs.general.PrefabMgr;

@SpirePatch(clz = AbstractRoom.class, method = "endBattle")
public class AtEndOfCombatPatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractRoom _inst) {
        PrefabMgr.receiveAtEndOfCombat(_inst);
    }
}