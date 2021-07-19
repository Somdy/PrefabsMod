package rs.prefabs.nemesis.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

public class ParalyticPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("Paralytic");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final float DMG_SCALE = 0.1F;
    private static final float REDUCE_DOM_SCALE = 0.001F;
    private static final float REDUCE_SCALE = -0.0006F;
    private float dmg_incrs;
    private float hp_reduce;
    
    public ParalyticPower(AbstractCreature owner, int amount) {
        setDefaults(POWER_ID, NAME, ExtraPowerType.SPECIAL);
        setValues(owner, amount);
        loadImg("Paralytic");
        checkScales();
        updateDescription();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && amount > 0) {
            checkScales();
            damage = damage + dmg_incrs;
            updateDescription();
        }
        return super.atDamageFinalReceive(damage, type);
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (amount > 0 && damageAmount > 0) {
            checkScales();
            if (damageAmount - MathUtils.floor(hp_reduce) >= 0) {
                damageAmount = damageAmount - MathUtils.ceil(damageAmount * hp_reduce);
                if (damageAmount < 0)
                    damageAmount = 0;
            }
            flash();
            addToBot(new ReducePowerAction(owner, owner, this, MathUtils.ceil(amount / 3F)));
            updateDescription();
        }
        return super.onLoseHp(damageAmount);
    }

    @Override
    public void updateDescription() {
        checkScales();
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[1] + MathUtils.floor(dmg_incrs)
                + DESCRIPTIONS[2] + SciPercent(hp_reduce) + DESCRIPTIONS[3];
    }

    private void checkScales() {
        dmg_incrs = MathUtils.floor(2F * (1F + DMG_SCALE * amount) + 0.5F);
        hp_reduce = REDUCE_DOM_SCALE * amount * amount + REDUCE_SCALE * amount;
        if (dmg_incrs < 0F)
            dmg_incrs = 0F;
        if (hp_reduce < 0)
            hp_reduce = 0;
        if (hp_reduce > 1F)
            hp_reduce = 1F;
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new ParalyticPower(owner, amount);
    }
}