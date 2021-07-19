package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.nemesis.actions.common.TeleperceiveAction;
import rs.prefabs.nemesis.interfaces.AbstractOffering;
import rs.prefabs.nemesis.powers.ParalyticPower;

public class Weirnelon extends AbstractNesGeneralCard implements AbstractOffering {
    private boolean originExhaustive;
    
    public Weirnelon() {
        super();
        originExhaustive = exhaust;
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new ApplyPowerAction(s, s, new ParalyticPower(s, magicNumber), magicNumber));
        addToBot(new TeleperceiveAction(ExMagicNum, cpr().drawPile, true, c -> getCardRealCost(c) < getCardRealCost(this)));
    }

    @Override
    protected void spectralize() {
        if (originExhaustive != exhaust)
            originExhaustive = exhaust;
        exhaust = true;
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        exhaust = originExhaustive;
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    public void triggerOnInitializationAndCompletion() {
        addToBot(new ApplyPowerAction(cpr(), cpr(), new ParalyticPower(cpr(), magicNumber), magicNumber));
        addToBot(new TeleperceiveAction(ExMagicNum, cpr().drawPile, true, c -> getCardRealCost(c) < getCardRealCost(this)));
    }
}