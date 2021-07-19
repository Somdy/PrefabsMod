package rs.prefabs.general.actions.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.utils.Debugger;
import rs.prefabs.nemesis.NesDebug;

import java.lang.reflect.Method;

public class ReinforceCardBlockAction extends AbstractPrefabGameAction {
    private AbstractCard card;

    public ReinforceCardBlockAction(AbstractCard card, int reinforcement) {
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
                Method ub = AbstractCard.class.getDeclaredMethod("upgradeBlock", int.class);
                ub.setAccessible(true);
                ub.invoke(card, amount);
            } catch (Exception e) {
                Debugger.Log("Failed to reinforce " + card.name + "'s block by using original methods");
                card.baseBlock += amount;
                card.upgradedBlock = true;
                e.printStackTrace();
            }
            card.flash();
        }
    }
}