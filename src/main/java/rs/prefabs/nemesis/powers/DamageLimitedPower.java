package rs.prefabs.nemesis.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

public class DamageLimitedPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("DamageLimited");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean percentage;
    private float reduce;
    
    public DamageLimitedPower(AbstractCreature owner, int turns) {
        setValues(owner, turns);
        loadImg("DamageLimited");
        updateDescription();
    }
    
    public DamageLimitedPower createPercentage(float reduce) {
        this.percentage = true;
        if (reduce > 1F) reduce = 1F;
        this.reduce = reduce;
        setDefaults(POWER_ID + SciPercent(reduce), NAME + ":" + SciPercent(reduce) + "%", PowerType.DEBUFF);
        updateDescription();
        return this;
    }
    
    public DamageLimitedPower createFixed(float reduce) {
        this.percentage = false;
        this.reduce = reduce;
        setDefaults(POWER_ID + SciPercent(reduce), NAME + ":" + SciPercent(reduce) + "%", PowerType.DEBUFF);
        updateDescription();
        return this;
    }

    @Override
    public void updateDescription() {
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[1]
                + (percentage ? SciPercent(reduce) + DESCRIPTIONS[2] : MathUtils.floor(reduce) + DESCRIPTIONS[3])
                + DESCRIPTIONS[4] + amount + DESCRIPTIONS[5];
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        if (damage > 0 && amount > 0) {
            if (percentage) {
                damage -= damage * reduce;
            } else {
                damage -= reduce;
            }
        }
        return super.atDamageFinalGive(damage, type);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (owner.isPlayer && isPlayer) {
            if (amount > 1)
                addToBot(new ReducePowerAction(owner, owner, this, 1));
            else 
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void atEndOfRound() {
        if (!owner.isPlayer) {
            if (amount > 1)
                addToBot(new ReducePowerAction(owner, owner, this, 1));
            else
                addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        DamageLimitedPower power = new DamageLimitedPower(owner, amount);
        if (percentage)
            power = power.createPercentage(reduce);
        else 
            power = power.createFixed(reduce);
        return power;
    }
}