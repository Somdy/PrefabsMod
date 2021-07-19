package rs.prefabs.general.actions.utility;

import rs.prefabs.general.abstracts.AbstractPrefabGameAction;

public class QuickAction extends AbstractPrefabGameAction {
    private Runnable action;

    public QuickAction(Runnable action, ActionType type, float dur) {
        this.action = action;
        if (type != null) this.actionType = type;
        duration = startDuration = dur;
    }

    public QuickAction(ActionType type) {
        this(null, type, 0F);
    }

    public QuickAction() {
        this(null, null, 0F);
    }

    public QuickAction setAction(Runnable action) {
        this.action = action;
        return this;
    }

    public QuickAction setDuration(float dur) {
        duration = startDuration = dur;
        return this;
    }

    @Override
    public void update() {
        isDone = true;
        if (action == null) {
            executeWhenJobsDone();
            return;
        }
        action.run();
        executeWhenJobsDone();
    }
}