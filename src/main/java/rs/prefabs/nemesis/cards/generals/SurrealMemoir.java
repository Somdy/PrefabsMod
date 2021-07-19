package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.nemesis.NesFab;

public class SurrealMemoir extends AbstractNesGeneralCard {
    public static final String ID = NesFab.makeID("SurrealMemoir");

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new DrawCardAction(s, magicNumber));
        if (canTriggerEnchantedEffect() && cpr().hand.size() <= ExMagicNum) return;
        addToBot(new DiscardAction(s, s, magicNumber, false));
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