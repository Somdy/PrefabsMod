package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;
import rs.prefabs.general.actions.common.DamageAndDoWhenUnblocked;
import rs.prefabs.nemesis.powers.ParalyticPower;

public class NamesOfGods extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new VFXAction(new LightningEffect(t.hb.cX, t.hb.cY)));
        addToBot(new VFXAction(new SearingBlowEffect(t.hb.cX, t.hb.cY, baseDamage / ExMagicNum)));
        addToBot(new DamageAndDoWhenUnblocked(t, crtDmgInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.FIRE, target -> {
            int amount = MathUtils.ceil(target.lastDamageTaken * (upgraded ? 0.5F : 0.25F));
            if (amount >= 1) {
                addToBot(new ApplyPowerAction(s, target, new ParalyticPower(s, amount)));
            }
        }));
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
        int originBaseDamage = baseDamage;
        int count = Math.toIntExact(cpr().masterDeck.group.stream().filter(c -> isCardRarityOf(c, CardRarity.RARE)).count());
        count = Math.min(count, ExMagicNum);
        baseDamage *= Math.pow(2, count);
        super.applyPowers();
        baseDamage = originBaseDamage;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int originBaseDamage = baseDamage;
        int count = Math.toIntExact(cpr().masterDeck.group.stream().filter(c -> isCardRarityOf(c, CardRarity.RARE)).count());
        count = Math.min(count, ExMagicNum);
        baseDamage *= Math.pow(2, count);
        super.calculateCardDamage(mo);
        baseDamage = originBaseDamage;
        isDamageModified = damage != baseDamage;
    }
}