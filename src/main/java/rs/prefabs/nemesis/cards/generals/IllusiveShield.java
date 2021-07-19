package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import rs.prefabs.nemesis.NesFab;

public class IllusiveShield extends AbstractNesGeneralCard {
    public static final String ID = NesFab.makeID("IllusiveShield");
    
    /*public IllusiveShield() {
        super();
        addTags(CardTags.STARTER_DEFEND);
    }*/

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, s, block));
        if (canTriggerEnchantedEffect()) {
            addToBot(new ApplyPowerAction(s, s, new DexterityPower(s, ExMagicNum), ExMagicNum));
            addToBot(new ApplyPowerAction(s, s, new LoseDexterityPower(s, ExMagicNum), ExMagicNum));
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
    public boolean canEnemyUse() {
        return true;
    }
}