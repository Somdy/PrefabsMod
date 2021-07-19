package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.cards.AbstractNesCard;

public class EidolonFormPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("EidolonForm");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final boolean upgraded;
    
    public EidolonFormPower(boolean upgraded) {
        setDefaults(POWER_ID, NAME, ExtraPowerType.SPECIAL);
        setValues(cpr(), -1);
        updateDescription();
        loadImg("TempBuff");
        this.upgraded = upgraded;
    }

    @Override
    public void updateDescription() {
        description = upgraded ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        if (card instanceof AbstractNesCard && ((AbstractNesCard) card).isEnchanted() && !upgraded) {
            return cpr().powers.stream().noneMatch(p -> isPowerTypeOf(p, PowerType.DEBUFF));
        }
        return super.canPlayCard(card);
    }

    @Override
    public AbstractPower makeCopy() {
        return new EidolonFormPower(upgraded);
    }
}