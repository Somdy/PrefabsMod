package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.common.SimpleHandCardSelectBuilder;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.tools.HandCardManipulator;
import rs.prefabs.nemesis.character.OfferHelper;
import rs.prefabs.nemesis.interfaces.OfferingContacter;
import rs.prefabs.nemesis.powers.GainEnergyAtTurnStartPower;

import java.util.ArrayList;
import java.util.List;

public class SiphonEnergy extends AbstractNesGeneralCard implements OfferingContacter {
    private List<AbstractCard> offerings;
    
    public SiphonEnergy() {
        super();
        setContentableOffer(true);
        offerings = new ArrayList<>();
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new SimpleHandCardSelectBuilder(canCardOffer)
                .setAmount(ExMagicNum)
                .setCanPickZero(false)
                .setAnyNumber(true)
                .setMsg(MSG[0])
                .setShouldMatchAll(true)
                .setManipulator(new HandCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int indexOfCard) {
                        boolean success;
                        if (canTriggerEnchantedEffect()) {
                            success = OfferHelper.AddOffering(card, magicNumber, crt -> {
                                addToBottom(new DrawCardAction(1));
                                addToBottom(new GainEnergyAction(1));
                            }, e -> MSG[1]);
                        } else {
                            success = OfferHelper.AddOffering(card, magicNumber);
                            if (success && !offerings.contains(card)) {
                                offerings.add(card);
                                addToBottom(new ApplyPowerAction(s, s, 
                                        new GainEnergyAtTurnStartPower(1, 1, MSG[3] + 1)));
                            }
                        }
                        return !success;
                    }
                })
                .setFollowUpAction(new DelayAction(() -> {
                    if (!offerings.isEmpty()) 
                        modifyContentedTip(offerings);
                })));
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
    public void onOfferingExhausted(AbstractCard offering) {
        if (offerings.contains(offering)) {
            offerings.remove(offering);
            if (offerings.isEmpty())
                setContentableOffer(true);
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        boolean isAllOfferingsExhausted = !OfferHelper.HasOfferings(offerings);
        if (!isAllOfferingsExhausted) {
            StringBuilder names = new StringBuilder(offerings.get(0).name);
            if (offerings.size() > 1) {
                names.append(", ");
                for (int i = 1; i < offerings.size(); i++) {
                    names.append(i == offerings.size() - 1 ? "" : ", ")
                            .append(offerings.get(i).name);
                }
            }
            cantUseMessage = names.toString() + MSG[2];
        }
        return canUse && isAllOfferingsExhausted;
    }
}