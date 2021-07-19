package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import rs.prefabs.nemesis.powers.PsychicPower;

public class Overhedge extends AbstractNesGeneralCard {
    private boolean originExhaustive;
    
    public Overhedge() {
        super();
        originExhaustive = exhaust;
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new ApplyPowerAction(s, s, new DexterityPower(s, -magicNumber), -magicNumber));
        addToBot(new ApplyPowerAction(s, s, new PsychicPower(s, ExMagicNum), ExMagicNum));
    }

    @Override
    public void promote() {
        super.promote();
        if (enchanted) {
            appendDescription(EXTENDED_DESCRIPTION[0]);
        }
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
    public boolean canEnemyUse() {
        return true;
    }
}