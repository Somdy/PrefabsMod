package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.nemesis.NesFab;

public class PuppetHealerPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("PuppetHealer");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int totalHealAmt;
    private int healedAmt;
    private int offsetHeal;
    
    public PuppetHealerPower(AbstractCreature owner, int totalHealAmt, int turns) {
        setDefaults(POWER_ID + totalHealAmt, NAME, PowerType.BUFF);
        setValues(owner, turns);
        this.totalHealAmt = totalHealAmt;
        this.healedAmt = 0;
        stackable = false;
        checkTurns();
        loadImg("TempBuff");
    }

    @Override
    public void atStartOfTurn() {
        if (healedAmt < totalHealAmt && !owner.isDeadOrEscaped()) {
            int heal = AbstractDungeon.miscRng.random(extraAmt - offsetHeal, extraAmt + offsetHeal);
            if (healedAmt + heal > totalHealAmt)
                heal = totalHealAmt - healedAmt;
            healedAmt += heal;
            addToBot(new HealAction(owner, owner, heal, 0.15F));
            addToBot(new ReducePowerAction(owner, owner, this, 1));
            addToBot(new DelayAction(this::checkTurns));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + extraAmt + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    private void checkTurns() {
        int per = (totalHealAmt - healedAmt) / amount;
        int offset = totalHealAmt - amount * per;
        offsetHeal = Math.max(offset, 1);
        extraAmt = per;
        updateDescription();
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new PuppetHealerPower(owner, totalHealAmt, amount);
    }
}