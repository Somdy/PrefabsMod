package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.patches.NesCustomEnum;

public class LoseHPNextTurnPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("LoseHPNextTurn");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public LoseHPNextTurnPower(AbstractCreature owner, int loseAmt, String name) {
        setDefaults(POWER_ID + loseAmt, name != null ? name : NAME, PowerType.DEBUFF);
        setValues(owner, loseAmt);
        loadImg("TempDebuff");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (amount > 0 && owner != null && !owner.isDeadOrEscaped()) {
            if (owner.isPlayer) {
                addToBot(new VFXAction(new OfferingEffect()));
                addToBot(new LoseHPAction(owner, owner, amount));
            } else {
                addToBot(new LoseHPAction(owner, owner, amount, NesCustomEnum.NES_PLAGUE));
            }
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new LoseHPNextTurnPower(owner, amount, name);
    }
}