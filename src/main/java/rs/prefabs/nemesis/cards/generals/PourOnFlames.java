package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.powers.HellfirePower;
import rs.prefabs.nemesis.powers.PourOnFlamesPower;

public class PourOnFlames extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new ApplyPowerAction(s, s, upgraded ? new PourOnFlamesPower(s, 0.33F)
                : new PourOnFlamesPower(s, 2)));
        if (canTriggerEnchantedEffect()) {
            addToBot(new QuickAction(AbstractGameAction.ActionType.DEBUFF)
                    .setAction(() -> {
                        for (AbstractCreature c : getAllLivingCreatures()) {
                            if (c.hasPower(HellfirePower.POWER_ID)) {
                                HellfirePower fire = (HellfirePower) c.getPower(HellfirePower.POWER_ID);
                                fire.incrsAmount(fire.amount);
                            }
                        }
                    }));
        }
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}