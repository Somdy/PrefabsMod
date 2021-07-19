package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.common.DamageAndDoWhenUnblocked;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.interfaces.IndicatorInvoker;
import rs.prefabs.nemesis.powers.HellfirePower;

public class ScorchingHeat extends AbstractNesGeneralCard implements IndicatorInvoker {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        active(new Vector2(current_x, current_y), crt -> 
                addToBot(new NullableSrcDamageAction(crt, crtDmgInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.FIRE)
                .setFollowupAction(new QuickAction(AbstractGameAction.ActionType.DAMAGE)
                        .setAction(() -> {
                            if (crt.hasPower(HellfirePower.POWER_ID)) {
                                for (AbstractCreature c : getAllExptCreatures(e -> e != crt)) {
                                    addToBot(new DamageAndDoWhenUnblocked(c, crtDmgInfo(s, damage, damageTypeForTurn), 
                                            AbstractGameAction.AttackEffect.FIRE, 
                                            target -> addToBot(new ApplyPowerAction(target, s, 
                                                    new HellfirePower(target, s, target.lastDamageTaken)))));
                                }
                            }
                        }))));
    }

    @Override
    protected void spectralize() {
        enchantDamage(0, baseDamage + 4);
    }

    @Override
    protected void despectralize() {
        disenchantDamage(0);
    }
}