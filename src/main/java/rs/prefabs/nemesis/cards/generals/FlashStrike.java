package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.nemesis.NesFab;

public class FlashStrike extends AbstractNesGeneralCard {
    public static final String ID = NesFab.makeID("FlashStrike");
    
    /*public FlashStrike() {
        super();
        addTags(CardTags.STARTER_STRIKE, CardTags.STRIKE);
    }*/

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new NullableSrcDamageAction(t, crtDmgInfo(s, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        if (canTriggerEnchantedEffect()) {
            addToBot(new ApplyPowerAction(t, s, new WeakPower(t, ExMagicNum, isInEnemyUse())));
        }
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
    public boolean canEnemyUse() {
        return true;
    }
}