package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.nemesis.NesFab;

public class BasaltWoundPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("BasaltWound");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public BasaltWoundPower(AbstractCreature owner, AbstractCreature source, int turns, int dmgAmt) {
        setDefaults(POWER_ID, NAME, PowerType.DEBUFF);
        setValues(owner, source, turns, dmgAmt);
        loadImg("TempDebuff");
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!owner.isDeadOrEscaped() && info.owner != null && info.owner == source
                && amount > 0 && extraAmt > 0) {
            flashWithoutSound();
            addToBot(new NullableSrcDamageAction(owner, crtDmgInfo(null, extraAmt, DamageInfo.DamageType.THORNS)));
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atEndOfRound() {
        if (!owner.isPlayer) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && owner.isPlayer) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (owner.isPlayer ? DESCRIPTIONS[1] : owner.name) + DESCRIPTIONS[2]
                + (source.isPlayer ? DESCRIPTIONS[1] : source.name) + DESCRIPTIONS[3] + extraAmt + DESCRIPTIONS[4]
                + amount + DESCRIPTIONS[5];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BasaltWoundPower(owner, source, amount, extraAmt);
    }
}