package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.nemesis.patches.NesCustomEnum;
import rs.prefabs.nemesis.powers.DecayPower;

public class Ghoscythe_Cursed extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new NullableSrcDamageAction(t, crtDmgInfo(s, damage, damageTypeForTurn), NesCustomEnum.NES_DECAY));
        addToBot(new ApplyPowerAction(t, s, new DecayPower(t, magicNumber)));
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int originBaseDamage = baseDamage;
        if (canTriggerEnchantedEffect() && mo.hasPower(DecayPower.POWER_ID)) {
            baseDamage *= ExMagicNum;
        }
        super.calculateCardDamage(mo);
        baseDamage = originBaseDamage;
        isDamageModified = damage != baseDamage;
    }
}