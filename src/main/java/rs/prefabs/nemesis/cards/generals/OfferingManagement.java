package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.SimpleGridCardSelectBuilder;
import rs.prefabs.general.tools.GridCardManipulator;
import rs.prefabs.nemesis.character.OfferHelper;

public class OfferingManagement extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new SimpleGridCardSelectBuilder(c -> true)
                .setAmount(magicNumber)
                .setCardGroup(OfferHelper.GetOfferingPack())
                .setDisplayInOrder(true)
                .setAnyNumber(true)
                .setCanCancel(true)
                .setMsg(MSG[0] + magicNumber + MSG[1] + (canTriggerEnchantedEffect() ? MSG[2] : ""))
                .setManipulator(new GridCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int indexOfCard, CardGroup group) {
                        AbstractCard c = card.makeStatEquivalentCopy();
                        c.isEthereal = true;
                        c.purgeOnUse = true;
                        if (canTriggerEnchantedEffect()) c.setCostForTurn(getCardRealCost(c) - 1);
                        addToTop(new MakeTempCardInHandAction(c, 1, true));
                        return false;
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