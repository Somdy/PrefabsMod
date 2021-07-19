package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.prefabs.general.interfaces.IndicatorInvoker;
import rs.prefabs.nemesis.powers.DamageLimitedPower;
import rs.prefabs.nemesis.powers.PsychicPower;

public class TraitorCloak extends AbstractNesGeneralCard implements IndicatorInvoker {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        active(new Vector2(current_x, current_y), crt -> {
            if (crt.isPlayer) {
                addToBot(new GainBlockAction(crt, s, block));
                addToBot(new ApplyPowerAction(crt, s, new StrengthPower(crt, -magicNumber)));
                addToBot(new ApplyPowerAction(crt, s, new GainStrengthPower(crt, magicNumber)));
            } else if (crt instanceof AbstractMonster) {
                addToBot(new ApplyPowerAction(crt, s, new StrengthPower(crt, ExMagicNum)));
                addToBot(new ApplyPowerAction(crt, s, new DamageLimitedPower(crt, 1).createPercentage(0.5F)));
            }
        });
        if (canTriggerEnchantedEffect()) {
            addToBot(new ApplyPowerAction(s, s, new PsychicPower(s, 2)));
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
}