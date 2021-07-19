package rs.prefabs.nemesis.dynamics;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.prefabs.nemesis.cards.AbstractNesCard;

public class NesWitherNum extends DynamicVariable {
    public static final String key = "nemesismod:WitherNum";

    @Override
    public String key() {
        return key;
    }

    @Override
    public boolean isModified(AbstractCard c) {
        return ((AbstractNesCard) c).isWitherNumModified();
    }

    @Override
    public int value(AbstractCard c) {
        return ((AbstractNesCard) c).getWitherNum();
    }

    @Override
    public int baseValue(AbstractCard c) {
        return ((AbstractNesCard) c).getBaseWitherNum();
    }

    @Override
    public boolean upgraded(AbstractCard c) {
        return ((AbstractNesCard) c).isUpgradedWitherNum();
    }
}