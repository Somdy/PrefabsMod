package rs.prefabs.nemesis.actions.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.nemesis.powers.HellfirePower;

public class GateOfForgeAction extends AbstractPrefabGameAction {
    private boolean noBlock;
    
    public GateOfForgeAction(int amount, AbstractCreature source, boolean noBlock) {
        this.amount = amount;
        this.source = source;
        this.noBlock = noBlock;
        actionType = ActionType.DEBUFF;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            for (AbstractCreature crt : getAllExptCreatures(c -> c != null && !c.isDeadOrEscaped()
                    && (!noBlock || c.currentBlock <= 0))) {
                addToBot(new ApplyPowerAction(crt, source, new HellfirePower(crt, source, amount)));
                addToBot(new DelayAction(() -> {
                    if (crt.getPower(HellfirePower.POWER_ID) != null) {
                        HellfirePower fire = (HellfirePower) crt.getPower(HellfirePower.POWER_ID);
                        fire.instantAttack();
                    }
                }));
            }
        }
        tickDuration();
    }
}