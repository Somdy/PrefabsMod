package rs.prefabs.nemesis.actions.common;

import com.megacrit.cardcrawl.core.Settings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.utils.Debugger;
import rs.prefabs.nemesis.NesDebug;
import rs.prefabs.nemesis.cards.AbstractNesCard;

import java.lang.reflect.Method;

public class ReinforceCardExMagicAction extends AbstractPrefabGameAction {
    private AbstractNesCard card;

    public ReinforceCardExMagicAction(AbstractNesCard card, int reinforcement) {
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
                Method uem = AbstractNesCard.class.getDeclaredMethod("upgradeExMagicNum", int.class);
                uem.setAccessible(true);
                uem.invoke(card, amount);
            } catch (Exception e) {
                NesDebug.Log("Failed to reinforce " + card.name + "'s exmagics by using original methods");
                card.setExMagicNumValue(card.getBaseExMagicNum() + amount, true);
                card.setUpgradedExMagicNum(true);
                e.printStackTrace();
            }
            card.flash();
        }
        tickDuration();
    }
}