package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.interfaces.TotemOffering;

public class DeadmanTotem extends AbstractNesGeneralCard implements TotemOffering {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, s, block));
        addToBot(new ApplyPowerAction(s, s, new FrailPower(s, magicNumber, isInEnemyUse())));
    }

    @Override
    protected void spectralize() {
        enchantBlock(0, baseBlock + 4);
    }

    @Override
    protected void despectralize() {
        disenchantBlock(0);
    }

    @Override
    public void triggerOnInitializationAndCompletion() {
        addToBot(new GainBlockAction(cpr(), cpr(), block));
        addToBot(new ApplyPowerAction(cpr(), cpr(), new FrailPower(cpr(), magicNumber, isInEnemyUse())));
    }

    @Override
    public void atStartOfTurn(AbstractCreature who, boolean postDraw) {
        if (who.isPlayer && postDraw) {
            addToBot(new QuickAction(AbstractGameAction.ActionType.BLOCK)
                    .setAction(() -> {
                        if (hasAnyPowerOf(cpr(), p -> isPowerTypeOf(p, AbstractPower.PowerType.DEBUFF))) {
                            flash();
                            addToBot(new GainBlockAction(cpr(), cpr(), block));
                        }
                    }));
        }
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}