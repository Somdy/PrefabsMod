package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.nemesis.actions.unique.HauntingVisionAction;
import rs.prefabs.nemesis.cards.AbstractNesCard;
import rs.prefabs.nemesis.interfaces.UndeadOffering;
import rs.prefabs.nemesis.powers.HauntedVisionPower;

public class HauntingVision extends AbstractNesGeneralCard implements UndeadOffering {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new HauntingVisionAction(magicNumber, !upgraded, 
                c -> getCardRealCost(c) <= 2 && (isCardTypeOf(c, CardType.ATTACK) || isCardTypeOf(c, CardType.SKILL)), c -> {
            if (canTriggerEnchantedEffect() && c instanceof AbstractNesCard && ((AbstractNesCard) c).canEnchant()) {
                ((AbstractNesCard) c).enchant();
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

    @Override
    public void triggerOnInitializationAndCompletion() {
        addToBot(new HauntingVisionAction(magicNumber, !upgraded,
                c -> getCardRealCost(c) <= 2 && (isCardTypeOf(c, CardType.ATTACK) || isCardTypeOf(c, CardType.SKILL)), c -> {
            if (canTriggerEnchantedEffect() && c instanceof AbstractNesCard && ((AbstractNesCard) c).canEnchant()) {
                ((AbstractNesCard) c).enchant();
            }
        }));
    }

    @Override
    public void triggerUndeadEffect() {
        addToBot(new MakeTempCardInDrawPileAction(this.makeCopy(), 1, true, true));
        addToBot(new ApplyPowerAction(cpr(), cpr(), new HauntedVisionPower(2)));
    }
}