package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.prefabs.general.actions.common.BetterDamageAllEnemiesAction;
import rs.prefabs.nemesis.NesFab;

public class Scorch extends AbstractNesGeneralCard {
    public static final String ID = NesFab.makeID("Scorch");

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new BetterDamageAllEnemiesAction(crtDmgInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.FIRE, false));
        /*addToBot(new DamageAllEnemiesAction(s, DamageInfo.createDamageMatrix(damage, false),
                damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));*/
        if (upgraded) {
            if (!isInEnemyUse()) {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                    addToBot(new ApplyPowerAction(m, s, new StrengthPower(m, -magicNumber), -magicNumber));
            } else {
                addToBot(new ApplyPowerAction(cpr(), s, new StrengthPower(cpr(), -magicNumber), -magicNumber));
            }
        }
    }

    @Override
    protected void spectralize() {
        enchantDamage(0, baseDamage + 1);
    }

    @Override
    protected void despectralize() {
        disenchantDamage(0);
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}
