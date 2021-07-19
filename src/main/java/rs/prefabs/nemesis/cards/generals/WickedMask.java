package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.interfaces.TotemOffering;
import rs.prefabs.nemesis.powers.DecayPower;
import rs.prefabs.nemesis.powers.ParalyticPower;

public class WickedMask extends AbstractNesGeneralCard implements TotemOffering {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction(AbstractGameAction.ActionType.DEBUFF)
                .setAction(() -> {
            for (AbstractCreature c : getAllLivingCreatures()) {
                addToBot(new ApplyPowerAction(c, s, new DecayPower(c, magicNumber)));
                addToBot(new ApplyPowerAction(c, s, new ParalyticPower(c, magicNumber)));
            }
        }));
        if (canTriggerEnchantedEffect())
            addToBot(new ApplyPowerAction(s, s, new ArtifactPower(s, 1)));
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
        addToBot(new QuickAction(AbstractGameAction.ActionType.DEBUFF)
                .setAction(() -> {
                    for (AbstractCreature c : getAllLivingCreatures()) {
                        addToBot(new ApplyPowerAction(c, cpr(), new DecayPower(c, magicNumber)));
                        addToBot(new ApplyPowerAction(c, cpr(), new ParalyticPower(c, magicNumber)));
                    }
                }));
        if (canTriggerEnchantedEffect())
            addToBot(new ApplyPowerAction(cpr(), cpr(), new ArtifactPower(cpr(), 1)));
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}