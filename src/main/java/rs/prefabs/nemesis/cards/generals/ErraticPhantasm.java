package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.nemesis.powers.ErraticPhantasmPower;
import rs.prefabs.nemesis.powers.PsychicPower;

public class ErraticPhantasm extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (canTriggerEnchantedEffect() && s.powers.stream().noneMatch(p -> p.ID.equals(PsychicPower.POWER_ID))) {
            addToBot(new GainBlockAction(s, s, block));
        }
        addToBot(new DelayAction(() -> {
            if (s.powers.stream().noneMatch(p -> p.ID.equals(ErraticPhantasmPower.POWER_ID))) {
                addToBot(new ApplyPowerAction(s, s, new ErraticPhantasmPower(s, ExMagicNum, magicNumber)));
            }
        }));
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }
}