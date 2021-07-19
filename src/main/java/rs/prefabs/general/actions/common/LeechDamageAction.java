package rs.prefabs.general.actions.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.misc.PrefabDmgInfo;

public class LeechDamageAction extends AbstractPrefabGameAction {
    private AttackEffect effect;
    private boolean useBiteEffect;
    private Color biteColor;
    
    public LeechDamageAction(AbstractCreature target, PrefabDmgInfo info, AttackEffect effect, boolean useBiteEffect) {
        this.target = target;
        this.source = info.owner;
        this.info = info;
        this.effect = effect;
        this.useBiteEffect = useBiteEffect;
        biteColor = Color.RED.cpy();
        actionType = ActionType.DAMAGE;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }
    
    public LeechDamageAction(AbstractCreature target, PrefabDmgInfo info, AttackEffect effect) {
        this(target, info, effect, true);
    }
    
    public LeechDamageAction setBiteColor(Color color) {
        this.biteColor = color;
        return this;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            if (useBiteEffect)
                effectToList(new BiteEffect(target.hb.cX, target.hb.cY - scale(40F), biteColor));
            effectToList(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, effect));
            target.damage(info);
            if (target.lastDamageTaken > 0) {
                int healAmt = MathUtils.floor(target.lastDamageTaken / 2F);
                addToTop(new HealAction(source, source, healAmt));
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        tickDuration();
    }
}