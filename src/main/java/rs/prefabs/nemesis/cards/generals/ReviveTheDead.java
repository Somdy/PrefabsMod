package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.utility.SimpleGridCardSelectBuilder;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.tools.GridCardManipulator;
import rs.prefabs.nemesis.character.OfferHelper;
import rs.prefabs.nemesis.interfaces.AbstractOffering;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviveTheDead extends AbstractNesGeneralCard {
    private static final int TURNS = 2;
    
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction().setAction(() -> {
            OfferHelper.RemoveAllOfferings();
            final int count = Math.min(magicNumber, OfferHelper.maxSlot);
            if (!upgraded) {
                List<AbstractCard> tmp = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    Optional<AbstractCard> card = getExptRandomCard(cardRandomRng(), c -> c instanceof AbstractOffering, 
                            OfferHelper.CardsOfferedThisCombat);
                    card.ifPresent(c -> tmp.add(c.makeStatEquivalentCopy()));
                }
                if (!tmp.isEmpty()) {
                    for (AbstractCard card : tmp) {
                        OfferHelper.AddOffering(card, TURNS);
                    }
                }
                return;
            }
            addToBot(new SimpleGridCardSelectBuilder(c -> c instanceof AbstractOffering)
                    .setAmount(count)
                    .setAnyNumber(true)
                    .setCanCancel(true)
                    .setCardGroup(OfferHelper.GetCardsOfferedThisCombat())
                    .setDisplayInOrder(true)
                    .setMsg(MSG[0] + count + MSG[1] + TURNS + MSG[2])
                    .setShouldMatchAll(true)
                    .setManipulator(new GridCardManipulator() {
                        @Override
                        public boolean manipulate(AbstractCard card, int indexOfCard, CardGroup group) {
                            OfferHelper.AddOffering(card, TURNS);
                            return false;
                        }
                    })
            );
        }));
        if (canTriggerEnchantedEffect()) {
            addToBot(new DelayAction(() -> OfferHelper.ResizeOfferingBox(1)));
        }
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