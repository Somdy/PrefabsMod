package rs.prefabs.nemesis.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.cards.DamageSource;
import rs.prefabs.general.cards.PrefabCardTags;
import rs.prefabs.general.misc.PrefabDmgInfo;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.actions.common.TeleperceiveAction;

public class ColdBloodedPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("ColdBlooded");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public ColdBloodedPower(int amount, int hpLoss) {
        setDefaults(POWER_ID, NAME, ExtraPowerType.SPECIAL);
        setValues(cpr(), amount, hpLoss);
        loadImg("TempBuff");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1]
                + (extraAmt > 0 ? DESCRIPTIONS[2] + extraAmt + DESCRIPTIONS[3] : null);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount > 0 && card.hasTag(PrefabCardTags.LEECH)) {
            flashWithoutSound();
            if (countSpecificCards(cpr().drawPile, c -> c.hasTag(PrefabCardTags.LEECH)) <= 0 && extraAmt > 0) {
                addToBot(new NullableSrcDamageAction(cpr(), new PrefabDmgInfo(new DamageSource(null), extraAmt, 
                        DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                return;
            }
            addToBot(new TeleperceiveAction(amount, cpr().drawPile, true, c -> c.hasTag(PrefabCardTags.LEECH)));
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new ColdBloodedPower(amount, extraAmt);
    }
}