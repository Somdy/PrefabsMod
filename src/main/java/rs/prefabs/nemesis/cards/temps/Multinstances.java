package rs.prefabs.nemesis.cards.temps;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.actions.utility.SimpleXCostActionBuilder;
import rs.prefabs.nemesis.powers.MultinstancesPower;

public class Multinstances extends AbstractNesTempCard {
    
    public Multinstances() {
        super();
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new SimpleXCostActionBuilder(freeToPlayOnce, energyOnUse, upgraded)
                .addEffect((index, originValue) -> originValue)
                .addUpgradeModifier((index, originValue) -> originValue + 1)
                .addAction(effect -> addToBot(new ApplyPowerAction(s, s, new MultinstancesPower(s, effect, magicNumber))))
                .build());
        if (canTriggerEnchantedEffect())
            addToBot(new GainEnergyAction(1));
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
}