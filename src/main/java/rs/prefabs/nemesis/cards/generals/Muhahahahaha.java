package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.common.MonsterInstantTakeTurnAction;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.annotations.Replaced;
import rs.prefabs.nemesis.powers.AdvancedVulnerablePower;

@Deprecated
@Replaced(substitute = DirtyBlood.class)
public class Muhahahahaha extends AbstractNesGeneralCard {
    
    public Muhahahahaha() {
        super();
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (canTriggerEnchantedEffect())
            addToBot(new GainBlockAction(s, s, block));
        addToBot(new DelayAction(() -> {
            addToBot(new ApplyPowerAction(t, s, new AdvancedVulnerablePower(t, upgraded ? 0.75F : 0.65F, magicNumber)));
            addToBot(new MonsterInstantTakeTurnAction((AbstractMonster) t));
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