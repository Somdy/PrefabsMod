package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.nemesis.actions.unique.GateOfForgeAction;

public class GateOfForge extends AbstractNesGeneralCard {
    
    public GateOfForge() {
        super();
        addTip(MSG[0], MSG[1]);
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GateOfForgeAction(magicNumber, s, upgraded));
    }

    @Override
    protected void spectralize() {
        enchantMagics(0, baseMagicNumber + 2);
    }

    @Override
    protected void despectralize() {
        disenchantMagics(0);
    }
}