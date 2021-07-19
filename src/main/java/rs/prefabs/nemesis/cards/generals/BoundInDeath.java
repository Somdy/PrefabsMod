package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.interfaces.IndicatorInvoker;
import rs.prefabs.nemesis.powers.DeathBoundMarkPower;

public class BoundInDeath extends AbstractNesGeneralCard implements IndicatorInvoker {
    private boolean originEthereal;
    
    public BoundInDeath() {
        super();
        addTip(MSG[0], MSG[1]);
        originEthereal = isEthereal;
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        active(new Vector2(current_x, current_y), crt -> 
                addToBot(new ApplyPowerAction(crt, s, new DeathBoundMarkPower(crt, magicNumber))));
    }

    @Override
    public void promote() {
        super.promote();
        if (enchanted) {
            if (originEthereal != isEthereal)
                originEthereal = isEthereal;
            isEthereal = false;
            subtractDescription(EXTENDED_DESCRIPTION[0]);
        }
    }

    @Override
    protected void spectralize() {
        if (originEthereal != isEthereal)
            originEthereal = isEthereal;
        isEthereal = false;
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        isEthereal = originEthereal;
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }
}