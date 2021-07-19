package rs.prefabs.general.tools;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.UUID;

public abstract class GridCardManipulator {
    private UUID uuid;

    public GridCardManipulator() {
        uuid = UUID.randomUUID();
    }

    /**
     * do something to the card chosen
     * @param card the card player chooses
     * @param indexOfCard the index of the card in the list chosen, usually start at 0
     * @param group the group where card is
     * @return whatever you want
     */
    public abstract boolean manipulate(AbstractCard card, int indexOfCard, CardGroup group);

    public void addToBottom(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }
}