package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.powers.DecayPower;
import rs.prefabs.nemesis.powers.HellfirePower;

import java.util.Optional;

public class SpectralCorporeity extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction(AbstractGameAction.ActionType.REDUCE_POWER).setAction(() -> {
            int count = 0;
            for (AbstractPower p : s.powers) {
                if (isPowerTypeOf(p, AbstractPower.PowerType.DEBUFF)) {
                    count++;
                    addToBot(new RemoveSpecificPowerAction(s, s, p));
                }
            }
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    Optional<AbstractCreature> target = getRandom(getAllExptCreatures(c -> c != s), cardRandomRng());
                    target.ifPresent(crt -> {
                        Optional<AbstractPower> power = upgraded ? getRandom(objsToList(new DecayPower(crt, magicNumber), 
                                new HellfirePower(crt, s, magicNumber))) : Optional.of(new DecayPower(crt, magicNumber));
                        power.ifPresent(p -> addToBot(new ApplyPowerAction(crt, s, p)));
                    });
                }
            }
        }));
    }

    @Override
    protected void spectralize() {
        enchantMagics(0, baseMagicNumber + 2);
    }

    @Override
    protected void despectralize() {
        disenchantMagics(0);
    }
}