package rs.prefabs.nemesis.cards.generals;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.common.BetterDamageAllEnemiesAction;
import rs.prefabs.general.actions.common.DrawUntilMeetsWhat;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.actions.utility.QuickAction;

public class DreadCome extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new DrawUntilMeetsWhat(new QuickAction(AbstractGameAction.ActionType.DAMAGE)
                .setAction(() -> {
                    int selfdmg = damage;
                    if (canTriggerEnchantedEffect()) selfdmg -= ExMagicNum;
                    if (selfdmg < 0) selfdmg = 0;
                    addToBot(new NullableSrcDamageAction(s, crtDmgInfo(s, selfdmg, damageTypeForTurn),
                            AbstractGameAction.AttackEffect.FIRE));
                    addToBot(new BetterDamageAllEnemiesAction(crtDmgInfo(s, damage, damageTypeForTurn), 
                            AbstractGameAction.AttackEffect.FIRE, false));
                    /*addToBot(new DamageAllEnemiesAction(s, DamageInfo.createDamageMatrix(damage, false),
                            damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));*/
                }), card -> !isCardTypeOf(card, CardType.ATTACK) || cpr().hand.size() >= BaseMod.MAX_HAND_SIZE));
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