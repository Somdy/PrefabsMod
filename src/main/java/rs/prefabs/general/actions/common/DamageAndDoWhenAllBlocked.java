package rs.prefabs.general.actions.common;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.misc.PrefabDmgInfo;

import java.util.function.Consumer;

public class DamageAndDoWhenAllBlocked extends AbstractPrefabGameAction {
    private AttackEffect effect;
    private Consumer<AbstractCreature> dowhat;

    public DamageAndDoWhenAllBlocked(AbstractCreature target, PrefabDmgInfo info, AttackEffect effect, Consumer<AbstractCreature> dowhat) {
        this.target = target;
        this.info = info;
        this.effect = effect;
        this.dowhat = dowhat;
        actionType = ActionType.DAMAGE;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            effectToList(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, effect, false));
            target.damage(info);
            if (info.source.isLeech() && target.lastDamageTaken > 0) {
                int healAmt = MathUtils.floor(target.lastDamageTaken / 2F);
                addToTop(new HealAction(info.owner, info.owner, healAmt));
            }
            if (target.lastDamageTaken <= 0)
                dowhat.accept(target);
        }
        tickDuration();
    }
}