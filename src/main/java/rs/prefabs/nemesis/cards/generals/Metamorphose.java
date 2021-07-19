package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.cards.AbstractNesCard;

import java.util.ArrayList;
import java.util.List;

public class Metamorphose extends AbstractNesGeneralCard {
    public static final String ID = NesFab.makeID("Metamorphose");

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new DrawCardAction(magicNumber, new QuickAction(AbstractGameAction.ActionType.CARD_MANIPULATION)
                .setAction(() -> {
                    List<AbstractCard> list = new ArrayList<>();
                    for (AbstractCard card : cpr().hand.group) {
                        if (isCardEnchantable(card) && !list.contains(card))
                            list.add(card);
                    }
                    if (list.isEmpty()) return;
                    for (int i = 0; i < ExMagicNum; i++) {
                        AbstractCard card = getRandom(list, cardRandomRng()).get();
                        enchantCard(card);
                        //card.enchant();
                        list.remove(card);
                        if (list.isEmpty()) break;
                    }
                })));
    }

    @Override
    protected void spectralize() {
        upgradeBaseCost(0);
    }

    @Override
    protected void despectralize() {
        upgradeNewCost();
    }
}