package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.common.DamageAndDoWhenUnblocked;
import rs.prefabs.general.actions.utility.SimpleHandCardSelectBuilder;
import rs.prefabs.general.tools.HandCardManipulator;
import rs.prefabs.nemesis.character.OfferHelper;
import rs.prefabs.nemesis.powers.HellfirePower;

public class IncendiaryDelight extends AbstractNesGeneralCard {
    private int originBaseCost;
    
    public IncendiaryDelight() {
        super();
        originBaseCost = cost;
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new SimpleHandCardSelectBuilder(canCardOffer)
                .setAmount(ExMagicNum)
                .setCanPickZero(false)
                .setAnyNumber(false)
                .setMsg(MSG[0])
                .setShouldMatchAll(true)
                .setManipulator(new HandCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int indexOfCard) {
                        boolean success = OfferHelper.AddOffering(card, magicNumber, c -> {
                            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                                if (!m.isDeadOrEscaped())
                                    addToBottom(new DamageAndDoWhenUnblocked(m, crtDmgInfo(s, IncendiaryDelight.this.damage, 
                                                    IncendiaryDelight.this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE, 
                                            crt -> {
                                        if (upgraded && !crt.isDeadOrEscaped())
                                            addToBottom(new ApplyPowerAction(crt, s, new HellfirePower(crt, s, crt.lastDamageTaken)));
                                    }));
                            }
                        }, e -> EXTENDED_DESCRIPTION[0] + IncendiaryDelight.this.damage
                                + (IncendiaryDelight.this.upgraded ? EXTENDED_DESCRIPTION[2] : EXTENDED_DESCRIPTION[1]));
                        return !success;
                    }
                }));
    }

    @Override
    protected void spectralize() {
        if (originBaseCost != cost)
            originBaseCost = cost;
        upgradeBaseCost(cost - 1);
    }

    @Override
    protected void despectralize() {
        setCostValue(originBaseCost, true);
    }
}