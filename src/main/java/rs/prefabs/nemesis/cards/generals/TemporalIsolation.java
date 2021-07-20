package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.common.SimpleHandCardSelectBuilder;
import rs.prefabs.general.tools.HandCardManipulator;
import rs.prefabs.nemesis.character.OfferHelper;

import java.util.ArrayList;
import java.util.List;

public class TemporalIsolation extends AbstractNesGeneralCard {
    public static List<AbstractCard> isolateds;
    
    public TemporalIsolation() {
        super();
        isolateds = new ArrayList<>();
        addTip(MSG[0], MSG[1]);
    }
    
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new SimpleHandCardSelectBuilder(c -> !isolateds.contains(c))
                .setAmount(magicNumber)
                .setAnyNumber(true)
                .setCanPickZero(true)
                .setShouldMatchAll(true)
                .setMsg(MSG[2])
                .setManipulator(new HandCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int indexOfCard) {
                        if (canTriggerEnchantedEffect() && indexOfCard == 0) card.selfRetain = true;
                        card.shuffleBackIntoDrawPile = true;
                        isolateds.add(card);
                        if (upgraded) OfferHelper.UnofferableCards.add(card);
                        return true;
                    }
                })
        );
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