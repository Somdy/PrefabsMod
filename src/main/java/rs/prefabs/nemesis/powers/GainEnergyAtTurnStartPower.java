package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

public class GainEnergyAtTurnStartPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("GainEnergyAtTurnStart");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public GainEnergyAtTurnStartPower(int energyGain, int turns, String name) {
        setDefaults(POWER_ID + energyGain + turns + name, name, PowerType.BUFF);
        setValues(cpr(), turns, energyGain);
        loadImg("TempBuff");
        updateDescription();
    }

    public GainEnergyAtTurnStartPower(int energyGain, int turns) {
        this(energyGain, turns, NAME);
    }

    @Override
    public void atStartOfTurn() {
        if (amount > 0 && extraAmt > 0 && !owner.isDeadOrEscaped()) {
            addToBot(new GainEnergyAction(extraAmt));
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        StringBuilder sbr = new StringBuilder(DESCRIPTIONS[0]);
        for (int i = 0; i < extraAmt; i++)
            sbr.append(" [E] ");
        sbr.append(DESCRIPTIONS[1]).append(amount).append(DESCRIPTIONS[2]);
        description = sbr.toString();
    }

    @Override
    public AbstractPower makeCopy() {
        return new GainEnergyAtTurnStartPower(extraAmt, amount, name);
    }
}