package rs.prefabs.nemesis.cards.temps;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.nemesis.actions.unique.RottenBoneAction;
import rs.prefabs.nemesis.interfaces.AbstractOffering;

public class RottenBone extends AbstractNesTempCard implements AbstractOffering {
    
    public RottenBone() {
        super();
        addTip(MSG[0], MSG[1]);
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new RottenBoneAction(magicNumber, s, canTriggerEnchantedEffect(), 
                crt -> crt != null && !crt.isDeadOrEscaped() && (isInEnemyUse() ? crt != enemyUser : crt != cpr())));
        if (upgraded)
            addToBot(new RottenBoneAction(magicNumber, s, canTriggerEnchantedEffect(),
                    crt -> crt != null && !crt.isDeadOrEscaped() && (isInEnemyUse() ? crt != enemyUser : crt != cpr())));
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }

    @NotNull
    @Override
    public Color getFavoriteColor() {
        return quickColor(232, 71, 15);
    }

    @Override
    public void triggerOnInitializationAndCompletion() {
        addToBot(new RottenBoneAction(magicNumber, cpr(), canTriggerEnchantedEffect(),
                crt -> crt != null && !crt.isDeadOrEscaped() && crt != cpr()));
        if (upgraded)
            addToBot(new RottenBoneAction(magicNumber, cpr(), canTriggerEnchantedEffect(),
                    crt -> crt != null && !crt.isDeadOrEscaped() && crt != cpr()));
    }
}