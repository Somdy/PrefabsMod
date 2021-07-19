package rs.prefabs.nemesis.actions.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoBlockPower;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.nemesis.NesDebug;

import java.lang.reflect.Field;

public class RunicCollarAction extends AbstractPrefabGameAction {
    private AbstractMonster target;
    private boolean guarantee;
    
    public RunicCollarAction(AbstractMonster t, int least, boolean guarantee) {
        this.target = t;
        this.amount = least;
        this.guarantee = guarantee;
        actionType = ActionType.BLOCK;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (target == null || target.isDeadOrEscaped()) {
            isDone = true;
            return;
        }
        if (target.getIntentDmg() <= 0) {
            isDone = true;
            return;
        }
        int dmg = target.getIntentDmg();
        int mult = 0;
        try {
            Field multiAmt = AbstractMonster.class.getDeclaredField("intentMultiAmt");
            multiAmt.setAccessible(true);
            mult = multiAmt.getInt(target);
        } catch (Exception ignored) {
            NesDebug.Log("Failed to get " + target.name + "'s attack times");
        } finally {
            if (mult <= 0 && dmg > 0) mult = 1;
        }
        if (dmg <= 0) {
            if (guarantee) {
                addToTop(new GainBlockAction(cpr(), cpr(), amount));
                addToBot(new ApplyPowerAction(cpr(), cpr(), new NoBlockPower(cpr(), 1, false)));
            }
            isDone = true;
            executeFollowUpAction();
            return;
        }
        for (int i = 0; i < mult; i++) {
            addToBot(new GainBlockAction(cpr(), cpr(), dmg));
        }
        addToBot(new ApplyPowerAction(cpr(), cpr(), new NoBlockPower(cpr(), 1, false)));
        isDone = true;
        executeFollowUpAction();
    }
}