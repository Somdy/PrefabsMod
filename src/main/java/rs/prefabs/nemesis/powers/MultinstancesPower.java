package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

public class MultinstancesPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("Multinstances");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public MultinstancesPower(AbstractCreature owner, int turns, int amount) {
        setDefaults(POWER_ID, NAME, PowerType.BUFF);
        setValues(owner, turns, amount);
        loadImg("TempBuff");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (owner != null && !owner.isDeadOrEscaped() && amount > 0) {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new PsychicPower(owner, extraAmt)));
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[1] + extraAmt + DESCRIPTIONS[2]
                + amount + DESCRIPTIONS[3];
    }

    @Override
    public AbstractPower makeCopy() {
        return new MultinstancesPower(owner, amount, extraAmt);
    }
}