package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.actions.common.ReinforceCardBlockAction;
import rs.prefabs.general.actions.common.ReinforceCardDamageAction;
import rs.prefabs.general.actions.common.ReinforceCardMagicAction;

public class CastawayWeapon extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new NullableSrcDamageAction(t, crtDmgInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new GainBlockAction(s, s, block));
        if (upgraded)
            addToBot(new ApplyPowerAction(t, s, new VulnerablePower(t, magicNumber, isInEnemyUse())));
    }

    @Override
    protected void spectralize() {
        enchantDamage(0, baseDamage + 3);
        enchantBlock(1, baseBlock + 2);
    }

    @Override
    protected void despectralize() {
        disenchantDamage(0);
        disenchantBlock(1);
    }

    /*@Override
    public void triggerPostCardDrawn(AbstractCard card) {
        if (canNourishCard(card)) {
            superFlash();
            if (baseDamage > 0) {
                Log("Reinforcing " + getWitherNum() + " damage to card: " + card.name);
                addToBot(new ReinforceCardDamageAction(card, getWitherNum()));
                upgradeDamage(-1);
            }
            if (baseBlock > 0) {
                Log("Reinforcing " + getWitherNum() + " block to card: " + card.name);
                addToBot(new ReinforceCardBlockAction(card, getWitherNum()));
                upgradeBlock(-1);
            }
            if (upgraded && baseMagicNumber > 0) {
                Log("Reinforcing " + getWitherNum() + " magics to card: " + card.name);
                addToBot(new ReinforceCardMagicAction(card, getWitherNum()));
                upgradeMagicNumber(-1);
            }
            reduceWither();
            if (hasCompletelyWithered()) {
                modifyWitheredDescription();
                upgradeBaseCost(0);
            }
        }
    }*/
}