package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class StubbornDefense extends AbstractNesGeneralCard {
    
    public StubbornDefense() {
        super();
        shuffleBackIntoDrawPile = true;
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, s, block));
        if (canTriggerEnchantedEffect())
            addToBot(new GainBlockAction(s, s, block, true));
    }

    @Override
    protected void spectralize() {
        enchantBlock(0, baseBlock / 2);
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        disenchantBlock(0);
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }
}