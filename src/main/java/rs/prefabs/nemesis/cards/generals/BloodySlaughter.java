package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;

public class BloodySlaughter extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new NullableSrcDamageAction(t, crtDmgInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
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
    public void applyPowers() {
        int originBaseDmg = baseDamage;
        baseDamage += ExMagicNum * countSpecificCards(cpr().hand, c -> isCardTypeOf(c, CardType.ATTACK) && c != this);
        super.applyPowers();
        baseDamage = originBaseDmg;
        isDamageModified = baseDamage != damage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int originBaseDmg = baseDamage;
        baseDamage += ExMagicNum * countSpecificCards(cpr().hand, c -> isCardTypeOf(c, CardType.ATTACK) && c != this);
        super.calculateCardDamage(mo);
        baseDamage = originBaseDmg;
        isDamageModified = baseDamage != damage;
    }
}