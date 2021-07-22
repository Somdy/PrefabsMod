package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import rs.prefabs.general.actions.utility.SimpleHandCardSelectBuilder;
import rs.prefabs.general.tools.HandCardManipulator;
import rs.prefabs.nemesis.character.OfferHelper;
import rs.prefabs.nemesis.interfaces.OfferingContacter;
import rs.prefabs.nemesis.powers.DelayGainStrengthPower;

public class CoercedGiving extends AbstractNesGeneralCard implements OfferingContacter {
    private AbstractCard offering;
    
    public CoercedGiving() {
        super();
        setContentableOffer(true);
        offering = null;
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToTop(new SimpleHandCardSelectBuilder(canCardOffer)
                .setAmount(ExMagicNum)
                .setCanPickZero(false)
                .setAnyNumber(false)
                .setMsg(MSG[0])
                .setShouldMatchAll(true)
                .setManipulator(new HandCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int indexOfCard) {
                        boolean success = OfferHelper.AddOffering(card, magicNumber);
                        if (success) {
                            offering = card;
                            modifyContentedTip(card);
                            int loss = -getCardRealCost(card);
                            if (loss >= 0) return false;
                            addToBottom(new VFXAction(s, new ShockWaveEffect(s.hb.cX, s.hb.cY, Settings.GREEN_TEXT_COLOR, 
                                    ShockWaveEffect.ShockWaveType.ADDITIVE), 0.15F));
                            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                                addToBottom(new ApplyPowerAction(m, s, new StrengthPower(m, loss), loss));
                                addToBottom(new ApplyPowerAction(m, s, (upgraded ? new DelayGainStrengthPower(m, -loss, -loss) : 
                                        new GainStrengthPower(m, -loss)), -loss));
                            }
                        }
                        return !success;
                    }
                }));
    }

    @Override
    protected void spectralize() {
        upgradeBaseCost(0);
    }

    @Override
    protected void despectralize() {
        upgradeNewCost();
    }

    @Override
    public void onOfferingExhausted(AbstractCard offering) {
        if (this.offering == offering) {
            setContentableOffer(true);
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        boolean isOfferingExhausted = !OfferHelper.HasOffering(offering);
        if (!isOfferingExhausted)
            cantUseMessage = offering.name + MSG[1];
        return canUse && isOfferingExhausted;
    }
}