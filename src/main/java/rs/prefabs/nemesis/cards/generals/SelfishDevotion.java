package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.SimpleHandCardSelectBuilder;
import rs.prefabs.general.tools.HandCardManipulator;
import rs.prefabs.nemesis.character.OfferHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SelfishDevotion extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new SimpleHandCardSelectBuilder(canCardOffer)
                .setAmount(1)
                .setCanPickZero(false)
                .setAnyNumber(false)
                .setMsg(MSG[0] + ExMagicNum + MSG[1])
                .setShouldMatchAll(true)
                .setManipulator(new HandCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int indexOfCard) {
                        boolean success = OfferHelper.AddOffering(card, magicNumber);
                        if (success) {
                            if (canTriggerEnchantedEffect()) {
                                AbstractCard copy = card.makeStatEquivalentCopy();
                                copy.purgeOnUse = true;
                                copy.isEthereal = true;
                                addToBottom(new MakeTempCardInHandAction(copy, 1, true));
                            }
                            List<AbstractCard> list = new ArrayList<>();
                            if (!cpr().masterDeck.isEmpty()) {
                                List<AbstractCard> cards = new ArrayList<>(cpr().masterDeck.group);
                                cards.removeIf(c -> !isCardEnchantable(c));
                                if (!cards.isEmpty()) {
                                    int amount = 0;
                                    while (cards.size() > 0 && amount < ExMagicNum) {
                                        Optional<AbstractCard> tmp = getRandomCard(cards, cardRandomRng());
                                        if (tmp.isPresent()) {
                                            if (cpr().masterDeck.contains(tmp.get()) && !list.contains(tmp.get())) {
                                                list.add(tmp.get());
                                                cards.remove(tmp.get());
                                                amount++;
                                                if (amount == ExMagicNum) break;
                                            }
                                        }
                                    }
                                }
                            }
                            if (!list.isEmpty()) {
                                for (AbstractCard c : list) {
                                    enchantCard(c);
                                    /*if (c instanceof AbstractNesCard)
                                        ((AbstractNesCard) c).enchant();*/
                                }
                            }
                        }
                        return !success;
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