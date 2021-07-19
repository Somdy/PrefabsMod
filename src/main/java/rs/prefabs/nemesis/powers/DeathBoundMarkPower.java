package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

public class DeathBoundMarkPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("DeathBoundMark");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private float dmgIncrs = 0.85F;
    private boolean invincible;
    
    public DeathBoundMarkPower(AbstractCreature owner, int turns) {
        setDefaults(POWER_ID, NAME, ExtraPowerType.SPECIAL);
        setValues(owner, turns);
        stackable = false;
        invincible = false;
        loadImg("TempSpecial");
        updateDescription();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (!invincible && amount > 0) {
            damage += damage * dmgIncrs;
        } else if (invincible) {
            damage = 0;
        }
        return super.atDamageFinalReceive(damage, type);
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (invincible) damageAmount = 0;
        return super.onLoseHp(damageAmount);
    }

    @Override
    public void atEndOfRound() {
        if (amount > 1 && !invincible) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        } else if (amount <= 1) {
            amount = -1;
            invincible = true;
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        if (!invincible) {
            description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + "\n"
                    + (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[3] + SciPercent(dmgIncrs) + DESCRIPTIONS[4];
            return;
        }
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[5];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DeathBoundMarkPower(owner, amount);
    }
}