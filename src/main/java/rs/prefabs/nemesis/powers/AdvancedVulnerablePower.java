package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

import java.util.Optional;

public class AdvancedVulnerablePower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("AdvancedVulnerable");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private float incrs;
    
    public AdvancedVulnerablePower(AbstractCreature owner, float incrs, int turns) {
        setDefaults(POWER_ID + incrs, NAME + ":" + SciPercent(incrs) + "%", PowerType.DEBUFF);
        setValues(owner, turns);
        this.incrs = incrs;
        loadImg("TempDebuff");
        updateDescription();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            damage += damage * incrs;
        }
        return super.atDamageFinalReceive(damage, type);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && owner.isPlayer) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void atEndOfRound() {
        if (!owner.isPlayer) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[1] + SciPercent(incrs) + DESCRIPTIONS[2];
    }

    @Override
    public void onInitialApplication() {
        if (!owner.isDeadOrEscaped() && hasAnyPowerOf(owner, p -> p instanceof AdvancedVulnerablePower && p != this)) {
            Optional<AbstractPower> p = getExptPower(owner, po -> po instanceof AdvancedVulnerablePower && po != this);
            p.ifPresent(po -> {
                if (po instanceof AdvancedVulnerablePower)
                    addToTop(new RemoveSpecificPowerAction(owner, owner, 
                            ((AdvancedVulnerablePower) po).incrs > this.incrs ? 
                            this : po));
            });
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new AdvancedVulnerablePower(owner, incrs, amount);
    }
}