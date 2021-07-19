package rs.prefabs.nemesis.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.cards.DamageSource;
import rs.prefabs.general.misc.PrefabDmgInfo;
import rs.prefabs.nemesis.NesFab;

public class PhantasmicMarkPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("PhantasmicMark");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean defensive;
    
    public PhantasmicMarkPower(AbstractCreature owner, AbstractCreature source, int turns, int blockAmt, boolean defensive) {
        setDefaults(POWER_ID + defensive, NAME + ":" + (defensive ? DESCRIPTIONS[0] : DESCRIPTIONS[1]), 
                defensive ? PowerType.BUFF : PowerType.DEBUFF);
        setValues(owner, source, defensive ? blockAmt : 0, turns);
        this.defensive = defensive;
        loadImg("PhantasmicMark");
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (extraAmt > 0 && !owner.isDeadOrEscaped() && defensive) {
            flash();
            addToBot(new GainBlockAction(owner, owner, amount, true));
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner == source && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0 && extraAmt > 0 && !defensive) {
            amount += MathUtils.floor(damageAmount / 2F);
            updateDescription();
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atStartOfTurn() {
        if (!defensive && amount > 0 && !owner.isDeadOrEscaped()) {
            flash();
            addToBot(new NullableSrcDamageAction(owner, new PrefabDmgInfo(new DamageSource(source), amount, 
                    DamageInfo.DamageType.THORNS), 
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            amount = 0;
            updateDescription();
        }
    }

    @Override
    public void atEndOfRound() {
        if (extraAmt > 1) {
            //TODO: Need an action for such power to reduce extra amount.
            extraAmt--;
            updateDescription();
            AbstractDungeon.onModifyPower();
        } else {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        if (defensive) {
            description = DESCRIPTIONS[2] + (owner.isPlayer ? DESCRIPTIONS[3] : owner.name + DESCRIPTIONS[3]) + amount
                    + DESCRIPTIONS[4] + DESCRIPTIONS[8] + extraAmt + DESCRIPTIONS[9];
        } else {
            description = (owner.isPlayer ? DESCRIPTIONS[5] : owner.name) + DESCRIPTIONS[6] + amount + DESCRIPTIONS[7]
                    + DESCRIPTIONS[8] + extraAmt + DESCRIPTIONS[9];
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        fontScale = 8.0F;
        if (defensive) {
            amount += stackAmount;
            if (amount > getMaxAmount()) amount = getMaxAmount();
        } else {
            extraAmt += stackAmount;
            if (extraAmt > getMaxAmount()) extraAmt = getMaxAmount();
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new PhantasmicMarkPower(owner, source, amount, extraAmt, defensive);
    }
}