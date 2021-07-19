package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.cards.AbstractNesCard;
import rs.prefabs.nemesis.powers.OverflowPower;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Overflow extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction(AbstractGameAction.ActionType.CARD_MANIPULATION)
                .setAction(() -> {
                    List<AbstractCard> list = new ArrayList<>();
                    for (AbstractCard card : cpr().hand.group) {
                        if (isCardEnchantable(card) && !list.contains(card))
                            list.add(card);
                    }
                    if (list.isEmpty()) return;
                    for (int i = 0; i < magicNumber; i++) {
                        Optional<AbstractCard> card = getRandomCard(list, cardRandomRng());
                        card.ifPresent(c -> {
                            enchantCard(c);
                            list.remove(c);
                        });
                        if (list.isEmpty()) break;
                    }
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
    public void triggerWhenDrawn() {
        if (canTriggerEnchantedEffect()) {
            addToTop(new ApplyPowerAction(cpr(), cpr(), new OverflowPower(1)));
            addToBot(new DelayAction(this::disenchant));
        }
    }

    @Override
    public void triggerOnManualDiscard() {
        if (upgraded) {
            addToBot(new QuickAction(AbstractGameAction.ActionType.CARD_MANIPULATION)
                    .setAction(() -> {
                        List<AbstractNesCard> list = new ArrayList<>();
                        for (AbstractCard card : cpr().drawPile.group) {
                            if (isCardEnchantable(card) && !list.contains(card))
                                list.add((AbstractNesCard) card);
                        }
                        if (list.isEmpty()) return;
                        Optional<AbstractNesCard> card = getRandomCard(list, cardRandomRng());
                        card.ifPresent(this::enchantCard);
                        list.clear();
                    }));
        }
    }
}