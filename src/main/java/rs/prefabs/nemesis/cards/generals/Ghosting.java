package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.SimpleXCostActionBuilder;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.cards.temps.Multinstances;
import rs.prefabs.nemesis.powers.PsychicPower;

public class Ghosting extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction()
                .setAction(() -> {
                    if (s.hasPower(PsychicPower.POWER_ID) && canTriggerEnchantedEffect())
                        addToBot(new MakeTempCardInDiscardAction(new Multinstances(), 1));
                }));
        addToBot(new DelayAction(() -> addToBot(new SimpleXCostActionBuilder(freeToPlayOnce, energyOnUse, upgraded)
                .addEffect((index, originValue) -> originValue)
                .addUpgradeModifier((index, originValue) -> originValue + 2)
                .addAction(effect -> addToBot(new ApplyPowerAction(s, s, new PsychicPower(s, effect))))
                .build())));
    }

    @Override
    protected void spectralize() {
        cardsToPreview = new Multinstances();
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        cardsToPreview = null;
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }
}