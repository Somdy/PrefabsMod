package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import rs.prefabs.nemesis.actions.unique.EidolonFormationAction;
import rs.prefabs.nemesis.powers.EidolonFormPower;

public class EidolonForm extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new EidolonFormationAction(upgraded));
        addToBot(new ApplyPowerAction(s, s, new EidolonFormPower(upgraded)));
        if (canTriggerEnchantedEffect())
            addToBot(new ApplyPowerAction(s, s, new ArtifactPower(s, magicNumber)));
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }
}