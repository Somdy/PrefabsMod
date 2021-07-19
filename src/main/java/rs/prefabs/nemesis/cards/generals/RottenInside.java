package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.powers.DecayPower;

public class RottenInside extends AbstractNesGeneralCard {
    private boolean originExhaustive;
    
    public RottenInside() {
        super();
        originExhaustive = exhaust;
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction(AbstractGameAction.ActionType.DEBUFF)
                .setAction(() -> {
                    if (t != null && !t.isDeadOrEscaped()) {
                        int amount = countSpecificPowerAmount(t, p -> isPowerTypeOf(p, AbstractPower.PowerType.DEBUFF));
                        if (amount > 0) {
                            addToBot(new ApplyPowerAction(t, s, new DecayPower(t, upgraded ? amount * 2 : amount)));
                        }
                    }
                }));
    }

    @Override
    public void promote() {
        super.promote();
        if (enchanted) {
            subtractDescription(EXTENDED_DESCRIPTION[0]);
        }
    }

    @Override
    protected void spectralize() {
        if (originExhaustive != exhaust)
            originExhaustive = exhaust;
        exhaust = false;
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        exhaust = originExhaustive;
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}