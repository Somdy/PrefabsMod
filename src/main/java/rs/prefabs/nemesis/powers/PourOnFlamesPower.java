package rs.prefabs.nemesis.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

public class PourOnFlamesPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("PourOnFlames");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean percentage;
    private float incrsment;
    
    public PourOnFlamesPower(AbstractCreature owner, int amount) {
        setDefaults(POWER_ID + false, NAME, PowerType.BUFF);
        setValues(owner, amount);
        this.percentage = false;
        loadImg("PourOnFlames");
        updateDescription();
    }

    public PourOnFlamesPower(AbstractCreature owner, float incrsment) {
        setDefaults(POWER_ID + true, NAME, PowerType.BUFF);
        setValues(owner, -1);
        this.incrsment = incrsment;
        this.percentage = true;
        loadImg("PourOnFlames");
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (owner.isPlayer && isPlayer) {
            for (AbstractCreature c : getAllLivingCreatures()) {
                if (c.hasPower(HellfirePower.POWER_ID)) {
                    HellfirePower fire = (HellfirePower) c.getPower(HellfirePower.POWER_ID);
                    if (fire.source == owner) {
                        flash();
                        if (!percentage) {
                            fire.incrsAmount(amount);
                            continue;
                        }
                        int incrs = MathUtils.ceil(fire.amount * incrsment);
                        fire.incrsAmount(incrs);
                    }
                }
            }
        }
    }

    @Override
    public void atEndOfRound() {
        if (!owner.isPlayer) {
            for (AbstractCreature c : getAllLivingCreatures()) {
                if (c.hasPower(HellfirePower.POWER_ID)) {
                    HellfirePower fire = (HellfirePower) c.getPower(HellfirePower.POWER_ID);
                    if (fire.source == owner) {
                        flash();
                        if (!percentage) {
                            fire.incrsAmount(amount);
                            continue;
                        }
                        int incrs = MathUtils.ceil(fire.amount * incrsment);
                        fire.incrsAmount(incrs);
                    }
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[1]
                + (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[2]
                + (percentage ? SciPercent(incrsment) + "%" : amount) + DESCRIPTIONS[3];
    }

    @Override
    public AbstractPower makeCopy() {
        return new PourOnFlamesPower(owner, amount);
    }
}