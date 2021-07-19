package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.listeners.UseCardListener;

public class Impatience extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction(AbstractGameAction.ActionType.ENERGY).setAction(() -> {
            addToTop(new GainEnergyAction(magicNumber));
            addToTop(new QuickAction().setAction(() -> 
                    UseCardListener.addNewCardPlayedEvent(cardID, true, 1, 
                            (card, action) -> addToBot(!card.isInAutoplay
                                    ? !(canTriggerEnchantedEffect() && isCardEnchanted(card)) 
                                    ? new LoseHPAction(s, s, 1)
                                    : null : null))));
        }));
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