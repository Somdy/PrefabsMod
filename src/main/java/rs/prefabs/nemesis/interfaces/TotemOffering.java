package rs.prefabs.nemesis.interfaces;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.jetbrains.annotations.NotNull;

public interface TotemOffering extends AbstractOffering {
    boolean withstand = false;
    boolean indestructible = false;
    
    default boolean isWithstand() {
        return withstand;
    }
    
    default boolean isIndestructible() {
        return indestructible;
    }
    
    default void onCardUse(AbstractCard card) {}
    
    default void postCardUsed(AbstractCard card, UseCardAction action) {}
    
    default void onOfferCard(AbstractCard card) {}
    
    default void postCardDrawn(AbstractCard card) {}
    
    default void atStartOfTurn(AbstractCreature who, boolean postDraw) {}
}