package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.actions.common.TeleperceiveAction;
import rs.prefabs.nemesis.cards.NesOptionCard;
import rs.prefabs.nemesis.interfaces.AbstractOffering;

public class GoldenApple extends AbstractNesGeneralCard implements AbstractOffering {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, s, block));
        if (!upgraded) {
            addToBot(new TeleperceiveAction(magicNumber, cpr().drawPile, false,
                    c -> getCardRealCost(c) < getCardRealCost(this) && isCardTypeOf(c, CardType.SKILL)));
            return;
        }
        addToBot(new QuickAction(AbstractGameAction.ActionType.CARD_MANIPULATION)
                .setAction(() -> {
                    NesOptionCard draw = NesOptionCard.createOption(name + ":" + MSG[0], EXTENDED_DESCRIPTION[0], textureImg, 
                            upgraded, enchanted, getFavoriteColor(), card -> addToBot(new TeleperceiveAction(magicNumber, 
                                    cpr().drawPile, false, c -> getCardRealCost(c) < getCardRealCost(this)
                                    && isCardTypeOf(c, CardType.SKILL))));
                    NesOptionCard discard = NesOptionCard.createOption(name + ":" + MSG[1], EXTENDED_DESCRIPTION[1], textureImg,
                            upgraded, enchanted, getFavoriteColor(), card -> addToBot(new TeleperceiveAction(magicNumber,
                                    cpr().discardPile, false, c -> getCardRealCost(c) < getCardRealCost(this)
                                    && isCardTypeOf(c, CardType.SKILL))));
                    addToBot(new ChooseOneAction(cardsToList(draw, discard)));
                }));
    }

    @Override
    protected void spectralize() {
        enchantBlock(0, baseBlock + 2);
        enchantMagics(1, baseMagicNumber + 1);
    }

    @Override
    protected void despectralize() {
        disenchantBlock(0);
        disenchantMagics(1);
    }

    @Override
    public void triggerOnInitializationAndCompletion() {
        addToBot(new GainBlockAction(cpr(), cpr(), block));
        if (!upgraded) {
            addToBot(new TeleperceiveAction(magicNumber, cpr().drawPile, false,
                    c -> getCardRealCost(c) < getCardRealCost(this) && isCardTypeOf(c, CardType.SKILL)));
            return;
        }
        addToBot(new QuickAction(AbstractGameAction.ActionType.CARD_MANIPULATION)
                .setAction(() -> {
                    NesOptionCard draw = NesOptionCard.createOption(name + ":" + MSG[0], EXTENDED_DESCRIPTION[0], textureImg,
                            upgraded, enchanted, getFavoriteColor(), card -> addToBot(new TeleperceiveAction(magicNumber,
                                    cpr().drawPile, false, c -> getCardRealCost(c) < getCardRealCost(this)
                                    && isCardTypeOf(c, CardType.SKILL))));
                    NesOptionCard discard = NesOptionCard.createOption(name + ":" + MSG[1], EXTENDED_DESCRIPTION[1], textureImg,
                            upgraded, enchanted, getFavoriteColor(), card -> addToBot(new TeleperceiveAction(magicNumber,
                                    cpr().discardPile, false, c -> getCardRealCost(c) < getCardRealCost(this)
                                    && isCardTypeOf(c, CardType.SKILL))));
                    addToBot(new ChooseOneAction(cardsToList(draw, discard)));
                }));
    }
}