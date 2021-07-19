package rs.prefabs.general.actions.common;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;

public class MonsterInstantTakeTurnAction extends AbstractPrefabGameAction {
    
    public MonsterInstantTakeTurnAction(AbstractMonster target) {
        this.target = target;
        actionType = ActionType.SPECIAL;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            if (target != null && !target.isDeadOrEscaped() && target instanceof AbstractMonster) {
                ((AbstractMonster) target).takeTurn();
            }
        }
        tickDuration();
    }
}