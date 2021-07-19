package rs.prefabs.general.patches.triggers;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.abstracts.AbstractPrefabPower;

public class OnGainBlockPatch {
    @SpirePatch(clz = AbstractCreature.class, method = "addBlock")
    public static class ModifyBlockPatch {
        @SpireInsertPatch(rloc = 1, localvars = {"tmp"})
        public static void Insert(AbstractCreature _inst, int blockAmount, @ByRef float[] tmp) {
            for (AbstractPower p : _inst.powers) {
                if (p instanceof AbstractPrefabPower)
                    tmp[0] = ((AbstractPrefabPower) p).modifyOnGainingBlock(tmp[0]);
            }
        }
    }
}