package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.actions.common.TeleperceiveAction;

public class LeafOnWater extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction().setAction(() -> {
            if (cpr().discardPile.isEmpty() && canTriggerEnchantedEffect()) {
                addToBot(new GainEnergyAction(1));
                return;
            }
            addToBot(new TeleperceiveAction(magicNumber, cpr().discardPile, !upgraded, c -> isCardTypeOf(c, CardType.SKILL) && c != this)
                    .setSideEffect(c -> c.setCostForTurn(getCardRealCost(c) - 1))
                    .setAnyNumber(false)
                    .setMsg(MSG[0]));
        }));
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }
}