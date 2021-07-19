package rs.prefabs.general.actions.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.utils.Debugger;
import rs.prefabs.nemesis.NesDebug;

import java.lang.reflect.Method;

public class ReinforceCardDamageAction extends AbstractPrefabGameAction {
    private AbstractCard card;

    public ReinforceCardDamageAction(AbstractCard card, int reinforcement) {
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
                Method ud = AbstractCard.class.getDeclaredMethod("upgradeDamage", int.class);
                ud.setAccessible(true);
                ud.invoke(card, amount);
            } catch (Exception e) {
                Debugger.Log("Failed to reinforce " + card.name + "'s damage by using original methods");
                card.baseDamage += amount;
                card.upgradedDamage = true;
                e.printStackTrace();
            }
            card.flash();
        }
    }
}