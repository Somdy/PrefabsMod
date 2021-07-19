package rs.prefabs.general.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnPlayCardSubscriber {
    void receiveOnPlayCard(AbstractCard card, AbstractCreature target, int energyOnUse);
}