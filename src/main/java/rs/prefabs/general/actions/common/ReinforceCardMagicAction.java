package rs.prefabs.general.actions.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.utils.Debugger;

import java.lang.reflect.Method;

public class ReinforceCardMagicAction extends AbstractPrefabGameAction {
    private AbstractCard card;

    public ReinforceCardMagicAction(AbstractCard card, int reinforcement) {
        this.card = card;
        this.amount = reinforcement;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            isDone = true;
            try {
                Method um = AbstractCard.class.getDeclaredMethod("upgradeMagicNumber", int.class);
                um.setAccessible(true);
                um.invoke(card, amount);
            } catch (Exception e) {
                Debugger.Log("Failed to reinforce " + card.name + "'s magics by using original methods");
                card.baseMagicNumber += amount;
                card.upgradedMagicNumber = true;
                e.printStackTrace();
            }
            card.flash();
        }
        tickDuration();
    }
}