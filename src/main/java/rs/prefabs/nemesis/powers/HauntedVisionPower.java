package rs.prefabs.nemesis.powers;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.nemesis.NesFab;

import java.lang.reflect.Field;

public class HauntedVisionPower extends AbstractNesPower {
    public static final String POWER_ID = NesFab.makeID("HauntedVision");
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public HauntedVisionPower(int amount) {
        setDefaults(POWER_ID, NAME, PowerType.DEBUFF);
        setValues(cpr(), amount);
        extraAmt = amount;
        loadImg("HauntedVision");
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        extraAmt += stackAmount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfRound() {
        extraAmt = amount;
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (extraAmt > 0) {
            extraAmt--;
            boolean success = obfuscate(card);
            if (!success) {
                Log("Failed to obfuscate card: " + card.name);
            }
        }
    }

    private boolean obfuscate(AbstractCard card) {
        if (!isCardTargetOf(card, AbstractCard.CardTarget.ENEMY, AbstractCard.CardTarget.SELF_AND_ENEMY)) {
            card.target = getRandom(objsToList(AbstractCard.CardTarget.ALL, AbstractCard.CardTarget.ALL_ENEMY, 
                    AbstractCard.CardTarget.NONE, AbstractCard.CardTarget.SELF)).get();
        }
        AbstractCard replacer = cpr().masterDeck.getRandomCard(true);
        if (!(replacer instanceof CustomCard)) {
            try {
                Field cardAtlas = AbstractCard.class.getDeclaredField("cardAtlas");
                cardAtlas.setAccessible(true);
                TextureAtlas atlas = (TextureAtlas) cardAtlas.get(replacer);
                card.portrait = atlas.findRegion(replacer.assetUrl);
            } catch (Exception ignored) {}
        } else if (card instanceof CustomCard) {
            ((CustomCard) card).loadCardImage(((CustomCard) replacer).textureImg);
        }
        card.rawDescription = replacer.rawDescription;
        card.type = replacer.type;
        card.name = replacer.name;
        card.initializeDescription();
        return true;
    }

    @Override
    public AbstractPower makeCopy() {
        return new HauntedVisionPower(amount);
    }
}