package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.actions.utility.SimpleHandCardSelectBuilder;
import rs.prefabs.general.annotations.Replaced;
import rs.prefabs.general.listeners.UseCardListener;
import rs.prefabs.general.tools.HandCardManipulator;
import rs.prefabs.nemesis.cards.AbstractNesCard;
import rs.prefabs.nemesis.patches.fixes.EnchantPreviewField;

@Deprecated
@Replaced(substitute = BloodyBlades.class)
public class Spectralize extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        EnchantPreviewField.forEnchant.set(AbstractDungeon.handCardSelectScreen, true);
        addToBot(new SimpleHandCardSelectBuilder(card -> card instanceof AbstractNesCard && ((AbstractNesCard) card).canEnchant())
                .setAmount(magicNumber)
                .setAnyNumber(true)
                .setCanPickZero(true)
                .setForUpgrade(true)
                .setShouldMatchAll(true)
                .setMsg(MSG[0] + (canTriggerEnchantedEffect() ? (upgraded ? MSG[2] : MSG[1]) : ""))
                .setManipulator(new HandCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int indexOfCard) {
                        if (isCardEnchantable(card)) {
                            //NesDebug.Log(this, "enchanting card: " + card.name);
                            ((AbstractNesCard) card).enchant();
                            UseCardListener.addCustomUnplayableCard(card, 1, (c, p, m) -> false, true);
                            if (canTriggerEnchantedEffect()) {
                                if (upgraded)
                                    card.selfRetain = true;
                                else 
                                    card.retain = true;
                            }
                        }
                        return true;
                    }
                }).setFollowupAction(new DelayAction(() ->
                        EnchantPreviewField.forEnchant.set(AbstractDungeon.handCardSelectScreen, false))));
    }

    @Override
    public void promote() {
        super.promote();
        if (enchanted && !enchantDesc.equals(EXTENDED_DESCRIPTION[1])) {
            subtractDescription(enchantDesc);
            appendDescription(EXTENDED_DESCRIPTION[1]);
        }
    }

    @Override
    protected void spectralize() {
        appendDescription(upgraded ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(upgraded ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[0]);
    }
}