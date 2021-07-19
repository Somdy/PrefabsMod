package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.nemesis.actions.unique.WarlikeAction;

public class Warlike extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new WarlikeAction(magicNumber, ExMagicNum));
    }

    @Override
    protected void spectralize() {
        enchantMagics(0, baseMagicNumber + 1);
    }

    @Override
    protected void despectralize() {
        disenchantMagics(0);
    }
}