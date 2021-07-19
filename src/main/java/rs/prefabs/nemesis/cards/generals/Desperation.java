package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.common.StackPowerAmountAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.interfaces.IndicatorInvoker;

public class Desperation extends AbstractNesGeneralCard implements IndicatorInvoker {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        active(new Vector2(current_x, current_y), crt -> {
            if (crt.powers.stream().anyMatch(p -> isPowerTypeOf(p, AbstractPower.PowerType.DEBUFF))) {
                addToBot(new QuickAction(AbstractGameAction.ActionType.POWER).setAction(() ->
                        crt.powers.stream().filter(p -> isPowerTypeOf(p, AbstractPower.PowerType.DEBUFF))
                                .forEach(p -> addToTop(new StackPowerAmountAction(crt, p, magicNumber)))));
                if (upgraded) {
                    addToBot(new QuickAction(AbstractGameAction.ActionType.POWER).setAction(() ->
                            crt.powers.stream().filter(p -> isPowerTypeOf(p, AbstractPower.PowerType.DEBUFF))
                                    .forEach(p -> addToTop(new StackPowerAmountAction(crt, p, magicNumber)))));
                }
            }
        }, crt -> crt.powers.stream().anyMatch(p -> isPowerTypeOf(p, AbstractPower.PowerType.DEBUFF)));
    }

    @Override
    protected void spectralize() {
        enchantMagics(0, baseMagicNumber + 2);
    }

    @Override
    protected void despectralize() {
        disenchantMagics(0);
    }

    @Override
    public boolean canEnchant() {
        return true;
    }
}