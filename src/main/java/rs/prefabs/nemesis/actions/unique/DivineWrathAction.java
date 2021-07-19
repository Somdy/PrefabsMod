package rs.prefabs.nemesis.actions.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.actions.common.DamageAndDoWhenUnblocked;
import rs.prefabs.general.cards.DamageSource;
import rs.prefabs.general.misc.PrefabDmgInfo;
import rs.prefabs.nemesis.powers.HellfirePower;

public class DivineWrathAction extends AbstractPrefabGameAction {
    private boolean applyHellfire;
    
    public DivineWrathAction(AbstractCreature target, PrefabDmgInfo info, boolean applyHellfire, int fires) {
        setValues(target, info);
        this.info = info;
        this.applyHellfire = applyHellfire;
        this.amount = fires;
        actionType = ActionType.DAMAGE;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            if (target == null || info == null) {
                isDone = true;
                return;
            }
            if (target.isDeadOrEscaped()) {
                isDone = true;
                return;
            }
            AbstractDungeon.effectList.add(new VerticalImpactEffect(target.hb.cX + target.hb.width / 4F,
                    target.hb.cY - target.hb.height / 4F));
        }
        tickDuration();
        if (isDone) {
            target.damage(info);
            if (target.lastDamageTaken > 0 && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                int dmg = target.lastDamageTaken;
                if (applyHellfire)
                    addToBot(new ApplyPowerAction(target, source, new HellfirePower(target, source, amount), amount));
                if (source.isPlayer) {
                    for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                        if (m != target)
                            addToBot(new DamageAndDoWhenUnblocked(m, crtDmgInfo(new DamageSource(source), dmg, 
                                    DamageInfo.DamageType.THORNS), 
                                    AttackEffect.FIRE, mo -> {
                                if (applyHellfire)
                                    addToBot(new ApplyPowerAction(mo, source, new HellfirePower(m, source, amount), amount));
                            }));
                    }
                }
            }
        }
    }
}