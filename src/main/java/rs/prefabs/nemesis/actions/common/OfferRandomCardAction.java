package rs.prefabs.nemesis.actions.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.nemesis.NesDebug;
import rs.prefabs.nemesis.character.OfferHelper;
import rs.prefabs.nemesis.interfaces.OfferingSideEffectDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OfferRandomCardAction extends AbstractPrefabGameAction {
    private int turns;
    private CardGroup where;
    private Consumer<AbstractCard> sideEffect;
    private OfferingSideEffectDescription effectDesc;
    
    public OfferRandomCardAction(int amount, int turns, CardGroup where) {
        this.amount = amount;
        this.turns = turns;
        this.where = where;
        sideEffect = null;
        effectDesc = null;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }

    public OfferRandomCardAction(int amount, int turns) {
        this(amount, turns, AbstractDungeon.player.hand);
    }
    
    public OfferRandomCardAction setSideEffect(Consumer<AbstractCard> sideEffect) {
        this.sideEffect = sideEffect;
        return this;
    }
    
    public OfferRandomCardAction setEffectDesc(String effectDesc) {
        this.effectDesc = card -> effectDesc;
        return this;
    }
    
    public OfferRandomCardAction setCardGroup(CardGroup where) {
        this.where = where;
        return this;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            if (where.isEmpty() || !OfferHelper.CanOffer()) {
                isDone = true;
                return;
            }
            List<AbstractCard> removeList = new ArrayList<>();
            if (where.size() <= amount) {
                for (AbstractCard card : where.group) {
                    boolean success = OfferHelper.AddOffering(card, turns, sideEffect, effectDesc);
                    if (success && !removeList.contains(card))
                        removeList.add(card);
                    else if (!success)
                        NesDebug.Log(this, "Unable to offer card: " + card.name + ", returning to where it was");
                }
                if (!removeList.isEmpty()) {
                    where.group.removeIf(removeList::contains);
                }
            } else if (where.size() > amount) {
                for (int i = 0; i < amount; i++) {
                    AbstractCard card = where.getRandomCard(cardRandomRng());
                    boolean success = OfferHelper.AddOffering(card, turns, sideEffect, effectDesc);
                    if (success && !removeList.contains(card))
                        removeList.add(card);
                    else if (!success)
                        NesDebug.Log(this, "Unable to offer card: " + card.name + ", returning to where it was");
                }
                if (!removeList.isEmpty()) {
                    where.group.removeIf(removeList::contains);
                }
            }
            isDone = true;
            executeWhenJobsDone();
        }
    }
}