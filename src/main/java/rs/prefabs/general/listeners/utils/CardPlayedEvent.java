package rs.prefabs.general.listeners.utils;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

@FunctionalInterface
public interface CardPlayedEvent {
    void onCardPlayed(AbstractCard card, UseCardAction action);
}
