package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.nemesis.actions.unique.LostPuppetAction;
import rs.prefabs.nemesis.interfaces.AbstractOffering;

public class LostPuppet extends AbstractNesGeneralCard implements AbstractOffering {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new LostPuppetAction(s, upgraded ? 0.5F : 0.25F, upgraded ? 1F : 0.5F, canTriggerEnchantedEffect(), magicNumber));
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
        addTip(MSG[0], MSG[1]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
        removeTip(MSG[0]);
    }

    @Override
    public void triggerOnInitializationAndCompletion() {
        addToBot(new LostPuppetAction(cpr(), upgraded ? 0.25F : 0.5F, upgraded ? 0.5F : 1F, canTriggerEnchantedEffect(), magicNumber));
    }
}