package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

public class ErraticPhantasmPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("ErraticPhantasm");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public ErraticPhantasmPower(AbstractCreature owner, int least, int most) {
        setDefaults(POWER_ID, NAME, PowerType.BUFF);
        setValues(owner, least, most);
        stackable = false;
        updateDescription();
        loadImg("TempBuff");
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && owner.isPlayer) {
            final int gain = extraAmt > amount ? cardRandomRng().random(amount, extraAmt) : amount;
            if (gain > 0 && owner != null && !owner.isDeadOrEscaped()) {
                flash();
                addToBot(new ApplyPowerAction(owner, owner, new PsychicPower(owner, gain)));
            }
        }
    }

    @Override
    public void atEndOfRound() {
        if (!owner.isPlayer) {
            final int gain = extraAmt > amount ? cardRandomRng().random(amount, extraAmt) : amount;
            if (gain > 0 && owner != null && !owner.isDeadOrEscaped()) {
                flash();
                addToBot(new ApplyPowerAction(owner, owner, new PsychicPower(owner, gain)));
            }
        }
    }

    @Override
    public float modifyOnGainingBlock(float blockAmount) {
        return 0;
    }

    @Override
    public void updateDescription() {
        final String name = owner.isPlayer ? DESCRIPTIONS[0] : owner.name;
        description = name + DESCRIPTIONS[1] + amount + (extraAmt > amount ? " ~ #b" + extraAmt : "")
                + DESCRIPTIONS[2] + name + DESCRIPTIONS[3];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ErraticPhantasmPower(owner, amount, extraAmt);
    }
}