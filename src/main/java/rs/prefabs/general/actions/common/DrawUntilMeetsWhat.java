package rs.prefabs.general.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;

import java.util.function.Predicate;

public class DrawUntilMeetsWhat extends AbstractPrefabGameAction {
    private AbstractGameAction action;
    private Predicate<AbstractCard> predicator;
    
    public DrawUntilMeetsWhat(AbstractGameAction action, Predicate<AbstractCard> predicator) {
        setValues(cpr(), cpr(), 0);
        this.action = action;
        this.predicator = predicator;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            addToBot(new DrawCardAction(1, action));
            if (!cpr().drawPile.isEmpty() && !predicator.test(cpr().drawPile.getTopCard()))
                addToBot(new DrawUntilMeetsWhat(action, predicator));
        }
        tickDuration();
    }
}