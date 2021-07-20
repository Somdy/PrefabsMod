package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.powers.BasaltWoundPower;

import java.util.List;

public class WoundedBasalt extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction(AbstractGameAction.ActionType.DEBUFF)
                .setAction(() -> {
                    List<AbstractCreature> tmp;
                    tmp = isInEnemyUse() ? 
                            getAllExptCreatures(c -> c.isPlayer && isCreatureWounded(c))
                            : getAllExptCreatures(c -> !c.isPlayer && isCreatureWounded(c));
                    if (!tmp.isEmpty()) {
                        for (AbstractCreature c : tmp) {
                            addToBot(new ApplyPowerAction(c, s, new BasaltWoundPower(c, s, magicNumber, ExMagicNum)));
                        }
                    }
                }));
    }

    @Override
    protected void spectralize() {
        enchantExMagics(0, baseExMagicNum + 2);
    }

    @Override
    protected void despectralize() {
        disenchantExMagics(0);
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}