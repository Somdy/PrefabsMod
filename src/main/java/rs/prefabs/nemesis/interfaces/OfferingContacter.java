package rs.prefabs.nemesis.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OfferingContacter {
    default void onOfferingExhausted(AbstractCard offering) {}
    default void onAddingNewOffering(AbstractCard offering) {}
}