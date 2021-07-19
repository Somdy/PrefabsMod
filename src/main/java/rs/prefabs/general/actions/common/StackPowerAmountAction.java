package rs.prefabs.general.actions.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;

import java.util.Optional;

public class StackPowerAmountAction extends AbstractPrefabGameAction {
    private AbstractPower power;
    
    public StackPowerAmountAction(AbstractCreature target, AbstractPower power, int amount) {
        this.target = target;
        this.power = power;
        this.amount = amount;
        actionType = ActionType.SPECIAL;
    }
    
    @Override
    public void update() {
        isDone = true;
        if (target.isDeadOrEscaped() || power == null || amount <= 0)
            return;
        Optional<AbstractPower> targetPower = target.powers.stream().filter(p -> p == power).findFirst();
        targetPower.ifPresent(p -> {
            p.stackPower(amount);
            p.updateDescription();
        });
        AbstractDungeon.onModifyPower();
    }
}