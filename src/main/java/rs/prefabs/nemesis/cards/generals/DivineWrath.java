package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import rs.prefabs.general.actions.common.ReinforceCardDamageAction;
import rs.prefabs.general.actions.common.ReinforceCardMagicAction;
import rs.prefabs.nemesis.actions.unique.DivineWrathAction;

public class DivineWrath extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (!isInEnemyUse()) {
            addToBot(new VFXAction(new VerticalImpactEffect(t.hb.cX + t.hb.width / 4F, t.hb.cY - t.hb.height / 4F)));
            addToBot(new DivineWrathAction(t, crtDmgInfo(s, damage, damageTypeForTurn), canTriggerEnchantedEffect(), magicNumber));
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

    /*@Override
    public void triggerPostCardDrawn(AbstractCard card) {
        if (canNourishCard(card)) {
            superFlash();
            if (baseDamage > 0) {
                Log("Reinforcing " + getWitherNum() + " damage to card: " + card.name);
                addToBot(new ReinforceCardDamageAction(card, getWitherNum()));
                upgradeDamage(-1);
            }
            if (isEnchanted() && baseMagicNumber > 0) {
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