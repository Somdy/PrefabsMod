package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.interfaces.IndicatorInvoker;
import rs.prefabs.nemesis.powers.HellfirePower;

public class SearingSaw extends AbstractNesGeneralCard implements IndicatorInvoker {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        for (int i = 0; i < ExMagicNum; i++) {
            addToBot(new VFXAction(new SearingBlowEffect(t.hb.cX, t.hb.cY, 3)));
            addToBot(new NullableSrcDamageAction(t, crtDmgInfo(s, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY));
            addToBot(new ApplyPowerAction(t, s, new HellfirePower(t, s, magicNumber)));
        }
    }

    @Override
    protected void spectralize() {
        enchantExMagics(0, baseExMagicNum + 1);
    }

    @Override
    protected void despectralize() {
        disenchantExMagics(0);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!upgraded && m != null) {
            if (m.currentBlock <= 0) {
                canUse = false;
                cantUseMessage = MSG[0];
            }
        }
        return canUse;
    }
}