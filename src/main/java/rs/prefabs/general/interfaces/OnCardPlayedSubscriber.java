package rs.prefabs.general.interfaces;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnCardPlayedSubscriber {
    void receiveOnCardPlayed(AbstractCard card, UseCardAction action);
}