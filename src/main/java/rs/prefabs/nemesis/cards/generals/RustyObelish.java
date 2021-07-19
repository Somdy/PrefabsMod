package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.nemesis.actions.unique.RustyObelishAction;
import rs.prefabs.nemesis.interfaces.DivineOffering;

public class RustyObelish extends AbstractNesGeneralCard implements DivineOffering {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new RustyObelishAction(magicNumber, color, canTriggerEnchantedEffect(), MSG[0], 
                card -> {
            if (upgraded) {
                card.isEthereal = true;
                card.purgeOnUse = true;
                card.setCostForTurn(Math.max(card.costForTurn - 1, 0));
            }
            else card.retain = true;
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

    @Override
    public void triggerOnInitializationAndCompletion() {
        addToBot(new RustyObelishAction(magicNumber, color, canTriggerEnchantedEffect(), MSG[0],
                card -> {
                    if (upgraded) {
                        card.isEthereal = true;
                        card.purgeOnUse = true;
                        card.setCostForTurn(Math.max(card.costForTurn - 1, 0));
                    }
                    else card.retain = true;
                }));
    }

    @Override
    public void triggerDivineEffect() {
        addToBot(new RustyObelishAction(MathUtils.ceil(magicNumber / 2F), CardColor.CURSE, false, MSG[0], 
                card -> card.selfRetain = true));
    }
}