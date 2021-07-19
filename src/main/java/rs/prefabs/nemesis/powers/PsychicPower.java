package rs.prefabs.nemesis.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

public class PsychicPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("Psychic");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final double mod = 0.25;
    private static final double div = 1.1;
    
    public PsychicPower(AbstractCreature owner, int amount) {
        setDefaults(POWER_ID, NAME, PowerType.BUFF);
        if (amount > getMaxAmount())
            amount = getMaxAmount();
        setValues(owner, amount);
        loadImg("Psychic");
        checkReduce();
        updateDescription();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        double reduce = damage - damage * (Math.log10(amount + mod) / div);
        return SciRound(reduce);
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new ReducePowerAction(owner, owner, this, extraAmt));
    }

    @Override
    public void updateDescription() {
        checkReduce();
        double reduce = (Math.log10(amount + mod) / div);
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name)
                + DESCRIPTIONS[1] + SciPercent(reduce) + DESCRIPTIONS[2] + extraAmt + DESCRIPTIONS[3];
    }

    @Override
    public void playApplyPowerSfx() {
        playSound(NesFab.makeID("Psychic_SFX"));
    }

    @Override
    public int getMaxAmount() {
        return 10;
    }
    
    private void checkReduce() {
        int reduce = 1;
        if (amount - reduce >= 5)
            reduce = MathUtils.ceil(amount / 2F);
        extraAmt = reduce;
    }

    @Override
    public AbstractPower makeCopy() {
        return new PsychicPower(owner, amount);
    }
}