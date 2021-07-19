package rs.prefabs.nemesis.actions.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;

import java.util.ArrayList;
import java.util.List;

public class WarlikeAction extends AbstractPrefabGameAction {
    private int strengths;
    
    public WarlikeAction(int armors, int strengths) {
        this.amount = armors;
        this.strengths = strengths;
        actionType = ActionType.SPECIAL;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            if (countSpecificCards(cpr().drawPile, c -> !isCardTypeOf(c, AbstractCard.CardType.ATTACK)) == 0) {
                isDone = true;
                return;
            }
            List<AbstractCard> tmp = new ArrayList<>();
            for (AbstractCard card : cpr().drawPile.group) {
                if (!isCardTypeOf(card, AbstractCard.CardType.ATTACK) && !tmp.contains(card))
                    tmp.add(card);
            }
            for (AbstractCard card : tmp) {
                if (cpr().drawPile.contains(card)) {
                    addToTop(new ExhaustSpecificCardAction(card, cpr().drawPile, true));
                    addToBot(new ApplyPowerAction(cpr(), cpr(), new PlatedArmorPower(cpr(), amount)));
                    addToBot(new ApplyPowerAction(cpr(), cpr(), new StrengthPower(cpr(), strengths)));
                }
            }
        }
        tickDuration();
    }
}