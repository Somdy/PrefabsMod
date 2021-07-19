package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.nemesis.interfaces.FranticItem;
import rs.prefabs.nemesis.powers.HellfirePower;

public class Firebolt extends AbstractNesGeneralCard implements FranticItem {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (upgraded) addToBot(new VFXAction(new LightningEffect(t.hb.cX, t.hb.cY)));
        addToBot(new NullableSrcDamageAction(t, crtDmgInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.FIRE));
        addToBot(new ApplyPowerAction(t, s, new HellfirePower(t, s, magicNumber)));
    }

    @Override
    protected void spectralize() {
        enchantMagics(0, baseMagicNumber + 1);
    }

    @Override
    protected void despectralize() {
        disenchantMagics(0);
    }

    @Override
    public float factor() {
        return upgraded ? 0.432F : 0.174F;
    }
}