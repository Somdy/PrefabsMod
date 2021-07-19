package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.listeners.ApplyPowerListener;
import rs.prefabs.nemesis.actions.unique.RunicCollarAction;
import rs.prefabs.nemesis.character.OfferHelper;
import rs.prefabs.nemesis.interfaces.TotemOffering;

import java.util.Optional;

public class RunicCollar extends AbstractNesGeneralCard implements TotemOffering {
    private static int rank;
    
    public RunicCollar() {
        super();
        rank = 0;
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (t instanceof AbstractMonster) {
            addToBot(new RunicCollarAction((AbstractMonster) t, block, canTriggerEnchantedEffect()));
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
    public void triggerOnInitializationAndCompletion() {
        addToBot(new QuickAction().setAction(() -> {
            Optional<AbstractMonster> target = getRandom(getAllLivingMstrs(), cardRandomRng());
            target.ifPresent(m -> addToBot(new RunicCollarAction(m, block, canTriggerEnchantedEffect())));
        }));
    }

    @Override
    public void onOfferInitialized() {
        addToBot(new QuickAction().setAction(() -> {
            ApplyPowerListener.addNewManipulator(getPrefabid() * "RunicCollar".length() + 1, 
                    rank, pm -> OfferHelper.HasOffering(this), 
                    (pow, tgt, src) -> {
                        if (pow != null && tgt == cpr()) {
                            flash();
                            if (isPowerTypeOf(pow, AbstractPower.PowerType.BUFF) && upgraded)
                                pow.amount = pow.amount >= 2 ? pow.amount / 2 : 1;
                            else if (!isPowerTypeOf(pow, AbstractPower.PowerType.BUFF) && upgraded)
                                pow = null;
                            else if (isPowerTypeOf(pow, AbstractPower.PowerType.BUFF) || isPowerTypeOf(pow, AbstractPower.PowerType.DEBUFF)
                                    && !upgraded)
                                pow = null;
                        }
                        return pow;
                    });
            rank++;
        }));
    }

    @Override
    public void onOfferCompleted() {
        if (!ApplyPowerListener.removeManipulator(getPrefabid() * "RunicCollar".length() + 1))
            Log("Failed to remove " + (getPrefabid() * "RunicCollar".length() + 1) + "-PowerMplr. It may be still working.");
    }
}