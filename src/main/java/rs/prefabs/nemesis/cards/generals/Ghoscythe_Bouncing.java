package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.common.BetterDamageAllEnemiesAction;
import rs.prefabs.general.actions.common.DamageAndDoWhenUnblocked;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.misc.PrefabDmgInfo;

public class Ghoscythe_Bouncing extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded) {
            addToBot(new DamageAndDoWhenUnblocked(t, crtDmgInfo(s, damage, damageTypeForTurn), 
                    AbstractGameAction.AttackEffect.SLASH_VERTICAL, c -> {
                if (c instanceof AbstractMonster) {
                    AbstractMonster m = AbstractDungeon.getRandomMonster((AbstractMonster) c);
                    if (m != null && !m.isDeadOrEscaped())
                        addToBot(new NullableSrcDamageAction(m, crtDmgInfo(s, damage, damageTypeForTurn), 
                                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
            }));
            return;
        }
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new DamageAndDoWhenUnblocked(t, crtDmgInfo(s, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_VERTICAL, c -> {
                if (c instanceof AbstractMonster) {
                    addToBot(new BetterDamageAllEnemiesAction(PrefabDmgInfo
                            .createInfoArray(crtDmgInfo(s, c.lastDamageTaken, DamageInfo.DamageType.THORNS)), 
                            AbstractGameAction.AttackEffect.SLASH_HEAVY, true, false));
                    /*addToBot(new DamageAllEnemiesAction(s, DamageInfo.createDamageMatrix(c.lastDamageTaken, true), 
                            DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY));*/
                }
            }));
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
}