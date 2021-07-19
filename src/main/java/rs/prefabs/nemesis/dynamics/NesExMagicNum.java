package rs.prefabs.nemesis.dynamics;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.prefabs.nemesis.cards.AbstractNesCard;

public class NesExMagicNum extends DynamicVariable {
    public static final String key = "nemesismod:ExMagic";
    
    @Override
    public String key() {
        return key;
    }

    @Override
    public boolean isModified(AbstractCard c) {
        return ((AbstractNesCard) c).isExMagicNumModified();
    }

    @Override
    public int value(AbstractCard c) {
        return ((AbstractNesCard) c).getExMagicNum();
    }

    @Override
    public int baseValue(AbstractCard c) {
        return ((AbstractNesCard) c).getBaseExMagicNum();
    }

    @Override
    public boolean upgraded(AbstractCard c) {
        return ((AbstractNesCard) c).isUpgradedExMagicNum();
    }
}