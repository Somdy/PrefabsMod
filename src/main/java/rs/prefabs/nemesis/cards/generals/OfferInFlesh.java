package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import rs.prefabs.general.actions.common.DamageAndDoWhenUnblocked;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.nemesis.actions.common.OfferRandomCardAction;

public class OfferInFlesh extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new VFXAction(new BiteEffect(t.hb.cX, t.hb.cY, Color.RED.cpy())));
        addToBot(new DamageAndDoWhenUnblocked(t, crtDmgInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.NONE, crt -> addToBot(new OfferRandomCardAction(magicNumber, ExMagicNum)
                        .setSideEffect(canTriggerEnchantedEffect() ? c -> {
                            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                                if (!m.isDeadOrEscaped() && m.currentHealth > s.currentHealth) {
                                    addToBot(new VFXAction(new LightningEffect(m.hb.cX, m.hb.cY)));
                                    addToBot(new NullableSrcDamageAction(m, crtDmgInfo(null, 5, 
                                            DamageInfo.DamageType.THORNS)));
                                }
                            }
                        } : null)
                        .setEffectDesc(canTriggerEnchantedEffect() ? EXTENDED_DESCRIPTION[1] : null))));
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }
}