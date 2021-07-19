package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.common.LeechDamageAction;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.cards.PrefabCardTags;
import rs.prefabs.nemesis.patches.NesCardEnum;
import rs.prefabs.nemesis.powers.ParalyticPower;

public class MindDrain extends AbstractNesGeneralCard {
    
    /*public MindDrain() {
        super();
        addTags(PrefabCardTags.LEECH);
    }*/

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new DrawCardAction(magicNumber, new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                for (AbstractCard card : DrawCardAction.drawnCards) {
                    if (getCardRealCost(card) > 0) {
                        int dmg = getCardRealCost(card);
                        addToBot(new NullableSrcDamageAction(t, crtDmgInfo(s, dmg, damageTypeForTurn), AttackEffect.NONE, 
                                true).setBiteColor(Color.DARK_GRAY.cpy()));
                        /*addToBot(new LeechDamageAction(t, new DamageInfo(s, dmg, damageTypeForTurn), AttackEffect.NONE, 
                                true).setBiteColor(Color.DARK_GRAY.cpy()));*/
                        if (upgraded) {
                            addToBot(new ApplyPowerAction(t, s, new ParalyticPower(t, dmg)));
                        }
                        if (canTriggerEnchantedEffect() && card.hasTag(PrefabCardTags.LEECH)) {
                            addToBot(new GainEnergyAction(1));
                        }
                    }
                }
            }
        }));
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