package rs.prefabs.general.patches.fixes;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = {DamageInfo.class})
public class OnLoseHpPowerFix {
    @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
    public static void Insert(AbstractMonster _inst, DamageInfo info, @ByRef int[] damageAmount) {
        if (damageAmount[0] > 0) {
            for (AbstractPower p : _inst.powers) {
                damageAmount[0] = p.onLoseHp(damageAmount[0]);
            }
        }
    }
    
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(Math.class, "min");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}