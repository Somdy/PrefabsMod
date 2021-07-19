package rs.prefabs.general.patches.fixes;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.abstracts.AbstractPrefabPower;

import java.lang.reflect.Field;

public class StackExtraPowerAmountFix {
    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class ApplyPowerActionStackPowerFix {
        @SpireInsertPatch(rloc = 78, localvars = {"p"})
        public static void Insert(ApplyPowerAction _inst, AbstractPower p) throws Exception {
            Field power = _inst.getClass().getDeclaredField("powerToApply");
            power.setAccessible(true);
            AbstractPower powerToApply = (AbstractPower) power.get(_inst);
            if (p.ID.equals(powerToApply.ID) && !p.ID.equals("Night Terror")
                    && p instanceof AbstractPrefabPower && powerToApply instanceof AbstractPrefabPower) {
                ((AbstractPrefabPower) p).stackExtraAmount(((AbstractPrefabPower) powerToApply).extraAmt);
                p.updateDescription();
            }
        }
    }
    @SpirePatch(clz = AbstractCreature.class, method = "addPower")
    public static class CreatureAddPowerStackFix {
        @SpireInsertPatch(rloc = 10, localvars = {"hasBuffAlready"})
        public static void Insert(AbstractCreature _inst, AbstractPower powerToApply, @ByRef boolean[] hasBuffAlready) {
            for (AbstractPower p : _inst.powers) {
                if (p.ID.equals(powerToApply.ID) && p instanceof AbstractPrefabPower && powerToApply instanceof AbstractPrefabPower) {
                    ((AbstractPrefabPower) p).stackExtraAmount(((AbstractPrefabPower) powerToApply).extraAmt);
                    p.updateDescription();
                    if (!hasBuffAlready[0])
                        hasBuffAlready[0] = true;
                }
            }
        }
    }
}