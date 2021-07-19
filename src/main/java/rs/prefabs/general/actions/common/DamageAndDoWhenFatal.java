package rs.prefabs.general.actions.common;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.misc.PrefabDmgInfo;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class DamageAndDoWhenFatal extends AbstractPrefabGameAction {
    private AttackEffect effect;
    private Consumer<AbstractCreature> dowhat;
    private Predicate<AbstractCreature> fatalJudge;
    
    public DamageAndDoWhenFatal(AbstractCreature target, PrefabDmgInfo info, AttackEffect effect, Consumer<AbstractCreature> dowhat,
                                Predicate<AbstractCreature> fatalJudge) {
        this.target = target;
        this.info = info;
        this.effect = effect;
        this.dowhat = dowhat;
        this.fatalJudge = fatalJudge;
        actionType = ActionType.DAMAGE;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }

    public DamageAndDoWhenFatal(AbstractCreature target, PrefabDmgInfo info, AttackEffect effect, Consumer<AbstractCreature> dowhat) {
        this(target, info, effect, dowhat, crt -> ((crt.isDying || crt.currentHealth <= 0)
                && !crt.halfDead && !crt.hasPower(MinionPower.POWER_ID)));
    }

    public DamageAndDoWhenFatal(AbstractCreature target, PrefabDmgInfo info, Consumer<AbstractCreature> dowhat) {
        this(target, info, AttackEffect.NONE, dowhat, crt -> ((crt.isDying || crt.currentHealth <= 0)
                && !crt.halfDead && !crt.hasPower(MinionPower.POWER_ID)));
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            if (target == null || target.isDeadOrEscaped() || info == null) {
                isDone = true;
                return;
            }
            effectToList(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, effect, false));
            target.damage(info);
            if (info.source.isLeech() && target.lastDamageTaken > 0) {
                int healAmt = MathUtils.floor(target.lastDamageTaken / 2F);
                addToTop(new HealAction(info.owner, info.owner, healAmt));
            }
            if (fatalJudge.test(target)) {
                dowhat.accept(target);
            }
        }
        tickDuration();
    }
}