package rs.prefabs.nemesis.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

public class DecayPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("Decay");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public DecayPower(AbstractCreature owner, int amount) {
        setDefaults(POWER_ID, NAME, PowerType.DEBUFF);
        setValues(owner, amount);
        loadImg("Decay");
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (amount > 0 && info.owner == owner && !owner.isDeadOrEscaped()) {
            int dmg = amount;
            int reduce = checkReduce(2F);
            flash();
            addToBot(new LoseHPAction(owner, null, dmg));
            if (reduce > 0)
                addToBot(new ReducePowerAction(owner, owner, this, reduce));
        }
    }
    
    public void instantDamage() {
        if (amount > 0 && !owner.isDeadOrEscaped()) {
            int dmg = amount;
            int reduce = checkReduce(4F);
            flash();
            addToBot(new LoseHPAction(owner, null, dmg));
            if (reduce > 0)
                addToBot(new ReducePowerAction(owner, owner, this, reduce));
        }
    }
    
    private int checkReduce(float d) {
        int reduce = MathUtils.ceil(amount / d);
        for (AbstractCreature crt : getAllExptCreatures(c -> c != null && !c.isDeadOrEscaped()
                && c.hasPower(CarrionPower.POWER_ID))) {
            int num = crt.getPower(CarrionPower.POWER_ID).amount;
            if (num > 0) {
                if (reduce - num > 0)
                    reduce -= num;
                else if (reduce - num <= 0)
                    reduce = 0;
            }
        }
        return reduce;
    }

    @Override
    public void updateDescription() {
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DecayPower(owner, amount);
    }
}