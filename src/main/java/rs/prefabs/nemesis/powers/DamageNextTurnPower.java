package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.misc.PrefabDmgInfo;
import rs.prefabs.nemesis.NesFab;

public class DamageNextTurnPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("DamageNextTurn");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private PrefabDmgInfo info;
    private AbstractGameAction.AttackEffect effect;
    
    public DamageNextTurnPower(AbstractCreature owner, @NotNull PrefabDmgInfo info, AbstractGameAction.AttackEffect effect,
                               String name) {
        setDefaults(POWER_ID, name != null ? name : NAME, PowerType.DEBUFF);
        setValues(owner, info.owner, info.base);
        this.info = info;
        this.effect = effect;
        loadImg("TempDebuff");
        updateDescription();
    }
    
    public DamageNextTurnPower(AbstractCreature owner, PrefabDmgInfo info, String name) {
        this(owner, info, AbstractGameAction.AttackEffect.NONE, name);
    }

    @Override
    public void atStartOfTurn() {
        if (amount > 0 && owner != null && !owner.isDeadOrEscaped()) {
            flash();
            addToBot(new NullableSrcDamageAction(owner, info, effect));
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = (owner.isPlayer ? DESCRIPTIONS[0] : owner.name) + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DamageNextTurnPower(owner, info, effect, name);
    }
}