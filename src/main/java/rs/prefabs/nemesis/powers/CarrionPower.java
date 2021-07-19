package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

public class CarrionPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("Carrion");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public CarrionPower(AbstractCreature owner, int amount) {
        setDefaults(POWER_ID, NAME, ExtraPowerType.SPECIAL);
        setValues(owner, amount);
        loadImg("TempSpecial");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new CarrionPower(owner, amount);
    }
}