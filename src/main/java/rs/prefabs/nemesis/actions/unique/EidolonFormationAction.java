package rs.prefabs.nemesis.actions.unique;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.nemesis.utils.NesGeneralUtils;

import java.util.ArrayList;
import java.util.List;

public class EidolonFormationAction extends AbstractPrefabGameAction implements NesGeneralUtils {
    private final boolean upgraded;
    
    public EidolonFormationAction(boolean upgraded) {
        this.upgraded = upgraded;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }
    
    @Override
    public void update() {
        isDone = true;
        List<AbstractCard> list = new ArrayList<>();
        list.addAll(cpr().hand.group);
        list.addAll(cpr().drawPile.group);
        list.addAll(cpr().discardPile.group);
        list.removeIf(c -> !isCardEnchantable(c));
        if (!list.isEmpty()) {
            list.forEach(c -> {
                if (upgraded) setEnchantmentTrigger(c, card -> cpr().powers.stream()
                        .noneMatch(p -> isPowerTypeOf(p, AbstractPower.PowerType.DEBUFF)));
                enchantCard(c);
            });
        }
    }
}