package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.nemesis.cards.temps.RottenBone;
import rs.prefabs.nemesis.powers.CarrionPower;

public class Carrion extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new ApplyPowerAction(s, s, new CarrionPower(s, magicNumber)));
    }

    @Override
    protected void spectralize() {
        cardsToPreview = new RottenBone();
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        cardsToPreview = null;
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }
}