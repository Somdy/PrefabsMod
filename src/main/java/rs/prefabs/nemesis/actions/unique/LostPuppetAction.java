package rs.prefabs.nemesis.actions.unique;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.cards.DamageSource;
import rs.prefabs.general.vfx.combat.LettingBloodEffect;
import rs.prefabs.nemesis.powers.PuppetHealerPower;

public class LostPuppetAction extends AbstractPrefabGameAction {
    private float percLife;
    private float percDmg;
    private boolean regen;
    
    public LostPuppetAction(AbstractCreature src, float percLife, float percDmg, boolean regen, int turns) {
        this.source = src;
        this.percLife = percLife;
        this.percDmg = percDmg;
        this.regen = regen;
        this.amount = turns;
        actionType = ActionType.DAMAGE;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            addToBot(new VFXAction(new LettingBloodEffect(source)));
            addToBot(new LoseHPAction(source, source, MathUtils.ceil(source.currentHealth * percLife)));
        }
        tickDuration();
        if (isDone) {
            addToBot(new DelayAction(() -> {
                if (!source.isDeadOrEscaped() && source.maxHealth > 0) {
                    int dmg = MathUtils.ceil(source.maxHealth * percDmg);
                    for (AbstractCreature c : getAllExptCreatures(crt -> crt != source && !crt.isDeadOrEscaped())) {
                        addToBot(new NullableSrcDamageAction(c, crtDmgInfo(new DamageSource(source), dmg, 
                                DamageInfo.DamageType.NORMAL), 
                                AttackEffect.FIRE));
                    }
                }
            }));
            if (regen)
                addToBot(new ApplyPowerAction(source, source, new PuppetHealerPower(source, source.lastDamageTaken, amount)));
        }
    }
}