package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.cards.AbstractNesCard;

public class OverflowPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("Overflow");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public OverflowPower(int amount) {
        setDefaults(POWER_ID, NAME, PowerType.BUFF);
        setValues(cpr(), amount);
        loadImg("TempBuff");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (isCardEnchantable(card) && amount > 0) {
            flash();
            addToBot(new QuickAction(AbstractGameAction.ActionType.CARD_MANIPULATION)
                    .setAction(() -> {
                        if (isCardEnchantable(card)) enchantCard(card);
                    }));
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new OverflowPower(amount);
    }
}