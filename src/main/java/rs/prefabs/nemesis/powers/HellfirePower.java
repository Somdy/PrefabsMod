package rs.prefabs.nemesis.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.common.DamageAndDoWhenSlightBlocked;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.cards.DamageSource;
import rs.prefabs.general.misc.PrefabDmgInfo;
import rs.prefabs.nemesis.NesFab;

public class HellfirePower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("Hellfire");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final float baseDmg = 2;
    private static float baseIncrs = 0.25F;
    
    public HellfirePower(AbstractCreature owner, AbstractCreature source, int amount) {
        setDefaults(POWER_ID, NAME, PowerType.DEBUFF);
        setValues(owner, source, amount);
        loadImg("Hellfire");
        checkDamage();
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && owner.isPlayer && amount > 0 && !owner.isDeadOrEscaped()) {
            flash();
            addToBot(new DamageAndDoWhenSlightBlocked(owner, new PrefabDmgInfo(new DamageSource(source), extraAmt, 
                    DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE, t -> {
                if (amount > 1)
                    addToBot(new ReducePowerAction(owner, owner, this, MathUtils.floor(amount / 2F)));
                else 
                    addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            }));
        }
    }

    @Override
    public void atEndOfRound() {
        if (!owner.isPlayer && amount > 0 && !owner.isDeadOrEscaped()) {
            flash();
            addToBot(new DamageAndDoWhenSlightBlocked(owner, new PrefabDmgInfo(new DamageSource(source), extraAmt, 
                    DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.FIRE, t -> {
                if (amount > 1)
                    addToBot(new ReducePowerAction(owner, owner, this, MathUtils.floor(amount / 2F)));
                else
                    addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            }));
        }
    }
    
    public void instantAttack() {
        flash();
        addToBot(new NullableSrcDamageAction(owner, new PrefabDmgInfo(new DamageSource(source), extraAmt, 
                DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
    }
    
    public void incrsAmount(int amount) {
        if (amount > 0) {
            flash();
            stackPower(amount);
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        checkDamage();
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[1] + extraAmt + DESCRIPTIONS[2];
    }
    
    private void checkDamage() {
        float base = baseDmg;
        float incrs = amount * baseIncrs;
        extraAmt = MathUtils.floor(base + base * incrs);
    }

    @Override
    public AbstractPower makeCopy() {
        return new HellfirePower(owner, source, amount);
    }
}