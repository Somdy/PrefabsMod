package rs.prefabs.general.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;

public class LatterAction extends AbstractPrefabGameAction {
    private AbstractGameAction action;

    public LatterAction(AbstractGameAction action, float dur) {
        this.action = action;
        this.duration = dur;
    }

    public LatterAction(AbstractGameAction action) {
        this(action, 0F);
    }

    @Override
    public void update() {

    }
}