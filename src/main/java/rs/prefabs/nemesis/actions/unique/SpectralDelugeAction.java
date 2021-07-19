package rs.prefabs.nemesis.actions.unique;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.listeners.UseCardListener;
import rs.prefabs.nemesis.cards.generals.SpectralDeluge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpectralDelugeAction extends AbstractPrefabGameAction {
    private boolean upgrade;
    
    public SpectralDelugeAction(int cost, boolean upgrade) {
        this.amount = cost;
        this.upgrade = upgrade;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            List<AbstractCard> tmp = new ArrayList<>();
            for (AbstractCard card : cpr().hand.group) {
                if (!tmp.contains(card))
                    tmp.add(card);
            }
            for (AbstractCard card : tmp) {
                if (cpr().hand.contains(card))
                    cpr().hand.moveToDiscardPile(card);
            }
            if (!UseCardListener.cardsPlayedLastTurn.isEmpty()) {
                int max = BaseMod.MAX_HAND_SIZE;
                int count = 0;
                while (count < max) {
                    Optional<AbstractCard> card = getExptRandomCard(cardRandomRng(), c -> !(c instanceof SpectralDeluge)
                            ,UseCardListener.cardsPlayedLastTurn);
                    if (card.isPresent()) {
                        if (amount != -99) card.get().setCostForTurn(amount);
                        else card.get().setCostForTurn(getCardRealCost(card.get()) - 1);
                        card.get().purgeOnUse = true;
                        card.get().isEthereal = true;
                        if (upgrade) card.get().upgrade();
                        addToBot(new MakeTempCardInHandAction(card.get(), 1, true));
                    }
                    count++;
                }
            }
        }
        tickDuration();
    }
}