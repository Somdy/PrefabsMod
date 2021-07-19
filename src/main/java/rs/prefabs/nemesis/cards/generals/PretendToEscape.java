package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.nemesis.actions.common.TeleperceiveAction;
import rs.prefabs.nemesis.powers.PsychicPower;

public class PretendToEscape extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new DiscardAction(s, s, magicNumber, !upgraded));
        addToBot(new ApplyPowerAction(s, s, new PsychicPower(s, ExMagicNum)));
        if (canTriggerEnchantedEffect()) {
            if (!upgraded) {
                addToBot(new DrawCardAction(1, new AbstractGameAction() {
                    @Override
                    public void update() {
                        isDone = true;
                        for (AbstractCard card : DrawCardAction.drawnCards) {
                            if (!card.retain)
                                card.retain = true;
                        }
                    }
                }));
            } else {
                addToBot(new TeleperceiveAction(1, cpr().drawPile, false, c -> true, c -> c.retain = true)
                        .setAnyNumber(false)
                        .setCanCancel(false)
                        .setMsg(MSG[0]));
            }
        }
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