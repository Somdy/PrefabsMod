package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.prefabs.nemesis.NesFab;

public class DelayGainStrengthPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("DelayGainStrength");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public DelayGainStrengthPower(AbstractCreature owner, int amount, int delays) {
        setDefaults(POWER_ID, NAME, PowerType.DEBUFF);
        if (amount > getMaxAmount())
            amount = getMaxAmount();
        setValues(owner, amount, delays);
        updateDescription();
        loadRegion("shackle");
    }

    public void playApplyPowerSfx() { 
        CardCrawlGame.sound.play("POWER_SHACKLE", 0.05F);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        flash();
        if (extraAmt <= 0) {
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            extraAmt--;
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[1] + extraAmt + DESCRIPTIONS[2] + 
                amount + DESCRIPTIONS[3];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DelayGainStrengthPower(owner, amount, extraAmt);
    }
}