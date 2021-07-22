package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.DualWield;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.actions.utility.SimpleHandCardSelectBuilder;
import rs.prefabs.general.tools.HandCardManipulator;
import rs.prefabs.nemesis.patches.fixes.EnchantPreviewField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cultivate extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (isInEnemyUse()) {
            //TODO
            return;
        }
        if (!upgraded) {
            randomEnchant();
            if (canTriggerEnchantedEffect())
                randomEnchant();
            return;
        }
        selectableEnchant();
        if (canTriggerEnchantedEffect())
            selectableEnchant();
    }
    
    private void selectableEnchant() {
        EnchantPreviewField.forEnchant.set(AbstractDungeon.handCardSelectScreen, true);
        addToBot(new SimpleHandCardSelectBuilder(this::isCardEnchantable)
                .setAmount(magicNumber)
                .setAnyNumber(true)
                .setCanPickZero(true)
                .setForUpgrade(true)
                .setShouldMatchAll(true)
                .setMsg(MSG[0])
                .setManipulator(new HandCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int indexOfCard) {
                        if (isCardEnchantable(card)) {
                            enchantCard(card);
                            //((AbstractNesCard) card).enchant();
                            //NesDebug.Log(this, card.name + " is enchanted ? " + ((AbstractNesCard) card).isEnchanted());
                        }
                        return true;
                    }
                }).setFollowupAction(new DelayAction(() -> 
                        EnchantPreviewField.forEnchant.set(AbstractDungeon.handCardSelectScreen, false))));
    }
    
    private void randomEnchant() {
        addToBot(new QuickAction(AbstractGameAction.ActionType.CARD_MANIPULATION)
                .setAction(() -> {
                    List<AbstractCard> list = new ArrayList<>();
                    for (AbstractCard card : cpr().hand.group) {
                        if (isCardEnchantable(card) && !list.contains(card))
                            list.add(card);
                    }
                    if (list.isEmpty()) return;
                    for (int i = 0; i < magicNumber; i++) {
                        Optional<AbstractCard> card = getRandomCard(list, cardRandomRng());
                        card.ifPresent(c -> {
                            enchantCard(c);
                            list.remove(c);
                        });
                        if (list.isEmpty()) break;
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
    public boolean canEnemyUse() {
        return true;
    }
}