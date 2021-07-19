package rs.prefabs.general.abstracts;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public abstract class AbstractPrefabGameEffect extends AbstractGameEffect {
    protected AbstractCreature source;
    protected AbstractCreature target;
}