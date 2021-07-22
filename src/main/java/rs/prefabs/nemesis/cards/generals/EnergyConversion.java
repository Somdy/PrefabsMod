package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.SimpleHandCardSelectBuilder;
import rs.prefabs.general.annotations.Replaced;
import rs.prefabs.general.tools.HandCardManipulator;
import rs.prefabs.nemesis.character.OfferHelper;

@Deprecated
@Replaced(substitute = SiphonEnergy.class)
public class EnergyConversion extends AbstractNesGeneralCard {
    
    public EnergyConversion() {
        super();
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new SimpleHandCardSelectBuilder(canCardOffer)
                .setAmount(magicNumber)
                .setCanPickZero(upgraded)
                .setAnyNumber(upgraded)
                .setMsg(MSG[0])
                .setShouldMatchAll(true)
                .setManipulator(new HandCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int indexOfCard) {
                        boolean success;
                        if (canTriggerEnchantedEffect()) {
                            success = OfferHelper.AddOffering(card, ExMagicNum, crt -> {
                                addToBottom(new DrawCardAction(1));
                                addToBottom(new GainEnergyAction(1));
                            }, e -> MSG[1]);
                        } else {
                            success = OfferHelper.AddOffering(card, ExMagicNum);
                        }
                        return !success;
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