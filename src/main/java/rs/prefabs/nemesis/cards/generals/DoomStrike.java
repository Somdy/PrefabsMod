package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import rs.prefabs.general.actions.common.BetterDamageAllEnemiesAction;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.annotations.Replaced;

@Deprecated
@Replaced(substitute = LeafOnWater.class)
public class DoomStrike extends AbstractNesGeneralCard {
    
    public DoomStrike() {
        super();
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (!isInEnemyUse()) {
            if (canTriggerEnchantedEffect())
                addToBot(new QuickAction(AbstractGameAction.ActionType.DEBUFF)
                        .setAction(() -> {
                            for (AbstractMonster m : getAllLivingMstrs()) {
                                addToBot(new ApplyPowerAction(m, s, new VulnerablePower(m, magicNumber, false)));
                            }
                        }));
            addToBot(new DelayAction(() -> {
                for (int i = 0; i < ExMagicNum; i++) {
                    addToBot(new BetterDamageAllEnemiesAction(crtDmgInfo(s, damage, damageTypeForTurn), 
                            AbstractGameAction.AttackEffect.SLASH_HEAVY, false));
                    /*addToBot(new DamageAllEnemiesAction(s, DamageInfo.createDamageMatrix(damage, false), 
                            damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));*/
                }
            }));
            return;
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