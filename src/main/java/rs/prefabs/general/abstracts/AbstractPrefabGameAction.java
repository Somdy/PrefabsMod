package rs.prefabs.general.abstracts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.prefabs.general.PrefabMgr;
import rs.prefabs.general.cards.DamageSource;
import rs.prefabs.general.misc.PrefabDmgInfo;
import rs.prefabs.general.utils.GeneralUtils;


public abstract class AbstractPrefabGameAction extends AbstractGameAction implements GeneralUtils {
    private AbstractGameAction followUpAction;
    public PrefabDmgInfo info;
    
    public AbstractPrefabGameAction setFollowupAction(AbstractGameAction action) {
        followUpAction = action;
        return this;
    }

    @Override
    protected void tickDuration() {
        super.tickDuration();
        if (isDone) {
            executeWhenJobsDone();
        }
    }
    
    protected void executeFollowUpAction() {
        if (followUpAction != null)
            addToBot(followUpAction);
    }
    
    protected void executeWhenJobsDone() {
        executeFollowUpAction();
        PrefabMgr.cleanAfterJobsDone();
    }
    
    protected PrefabDmgInfo crtDmgInfo(DamageSource source, int base, DamageInfo.DamageType type) {
        return new PrefabDmgInfo(source, base, type);
    }
    
    protected AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }
}