package rs.prefabs.general.tools;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;
import java.util.UUID;

public abstract class HandCardManipulator {
    private UUID uuid;

    public HandCardManipulator() {
        uuid = UUID.randomUUID();
    }

    /**
     * do something to the card chosen
     * @param card the card the player chooses
     * @param indexOfCard the index of the card in the list chosen, usually start at 0
     * @return true if the card should return to hand, false if the card should not return to hand
     */
    public abstract boolean manipulate(AbstractCard card, int indexOfCard);

    public void addToBottom(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }
}