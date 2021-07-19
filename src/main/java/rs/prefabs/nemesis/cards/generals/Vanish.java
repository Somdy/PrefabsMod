package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import rs.prefabs.nemesis.powers.PsychicPower;

public class Vanish extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new ApplyPowerAction(s, s, new PsychicPower(s, magicNumber), magicNumber));
        if (canTriggerEnchantedEffect()) {
            addToBot(new ApplyPowerAction(s, s, new PsychicPower(s, ExMagicNum), ExMagicNum));
            addToBot(new ApplyPowerAction(s, s, new VulnerablePower(s, magicNumber, isInEnemyUse()), magicNumber));
        }
        if (!upgraded && !isInEnemyUse())
            addToBot(new PressEndTurnButtonAction());
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}