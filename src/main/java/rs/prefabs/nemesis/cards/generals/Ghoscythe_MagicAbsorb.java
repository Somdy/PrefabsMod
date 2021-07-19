package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.nemesis.cards.AbstractNesCard;

public class Ghoscythe_MagicAbsorb extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (!upgraded) {
            addToBot(new NullableSrcDamageAction(t, crtDmgInfo(s, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY));
            return;
        }
        addToBot(new NullableSrcDamageAction(AbstractDungeon.getRandomMonster(), crtDmgInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    @Override
    public void applyPowers() {
        int realBaseDamage = baseDamage;
        int incrs = countSpecificCards(cpr().hand, c -> c instanceof AbstractNesCard && ((AbstractNesCard) c).isEnchanted());
        if (upgraded) {
            incrs += countSpecificCards(cpr().drawPile, c -> c instanceof AbstractNesCard && ((AbstractNesCard) c).isEnchanted());
            incrs += countSpecificCards(cpr().discardPile, c -> c instanceof AbstractNesCard && ((AbstractNesCard) c).isEnchanted());
        }
        baseDamage = incrs * baseDamage;
        super.applyPowers();
        baseDamage = realBaseDamage;
        isDamageModified = baseDamage != damage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = baseDamage;
        int incrs = countSpecificCards(cpr().hand, c -> c instanceof AbstractNesCard && ((AbstractNesCard) c).isEnchanted());
        if (upgraded) {
            incrs += countSpecificCards(cpr().drawPile, c -> c instanceof AbstractNesCard && ((AbstractNesCard) c).isEnchanted());
            incrs += countSpecificCards(cpr().discardPile, c -> c instanceof AbstractNesCard && ((AbstractNesCard) c).isEnchanted());
        }
        baseDamage = incrs * baseDamage;
        super.calculateCardDamage(mo);
        baseDamage = realBaseDamage;
        isDamageModified = baseDamage != damage;
    }

    @Override
    protected void spectralize() {
        enchantDamage(0, baseDamage + 2);
    }

    @Override
    protected void despectralize() {
        disenchantDamage(0);
    }
}