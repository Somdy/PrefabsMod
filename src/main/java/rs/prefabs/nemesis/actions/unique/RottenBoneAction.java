package rs.prefabs.nemesis.actions.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.nemesis.powers.DecayPower;

import java.util.Optional;
import java.util.function.Predicate;

public class RottenBoneAction extends AbstractPrefabGameAction {
    private boolean damage;
    private Predicate<AbstractCreature> toWhom;
    
    public RottenBoneAction(int amount, AbstractCreature source, boolean damage, Predicate<AbstractCreature> toWhom) {
        this.amount = amount;
        this.source = source;
        this.damage = damage;
        this.toWhom = toWhom;
        actionType = ActionType.SPECIAL;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            Optional<AbstractCreature> crt = getRandom(getAllExptCreatures(toWhom), cardRandomRng());
            if (crt.isPresent() && !crt.get().isDeadOrEscaped()) {
                addToTop(new ApplyPowerAction(crt.get(), source, new DecayPower(crt.get(), amount)));
                if (damage)
                    addToBot(new DelayAction(() -> {
                        if (crt.get().hasPower(DecayPower.POWER_ID)) {
                            DecayPower decay = (DecayPower) crt.get().getPower(DecayPower.POWER_ID);
                            decay.instantDamage();
                        }
                    }));
            }
        }
        tickDuration();
    }
}