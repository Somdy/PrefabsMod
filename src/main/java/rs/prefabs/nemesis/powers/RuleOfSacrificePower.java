package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.NesFab;

public class RuleOfSacrificePower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("RuleOfSacrifice");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean allEnemies;
    
    public RuleOfSacrificePower(int amount, boolean allEnemies) {
        setDefaults(POWER_ID, NAME, PowerType.BUFF);
        setValues(cpr(), amount);
        this.allEnemies = allEnemies;
        stackable = false;
        loadImg("TempBuff");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = allEnemies ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }

    @Override
    public void afterCardPutOnOffer(AbstractCard card, int turns) {
        if (allEnemies) {
            addToBot(new QuickAction().setAction(() -> {
                for (AbstractMonster m : getAllLivingMstrs())
                    addToTop(new NewQueueCardAction(card, m, true, true));
            }));
            return;
        }
        addToBot(new QuickAction().setAction(() -> addToTop(new NewQueueCardAction(card, AbstractDungeon.getRandomMonster(),
                true, true))));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer)
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    public void upgrade() {
        if (!allEnemies) {
            allEnemies = true;
            flash();
            updateDescription();
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new RuleOfSacrificePower(amount, allEnemies);
    }
}