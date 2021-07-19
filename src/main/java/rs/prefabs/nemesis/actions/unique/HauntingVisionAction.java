package rs.prefabs.nemesis.actions.unique;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.nemesis.actions.common.TeleperceiveAction;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class HauntingVisionAction extends AbstractPrefabGameAction {
    private boolean random;
    private Predicate<AbstractCard> predicator;
    private Consumer<AbstractCard> dowhat;
    
    public HauntingVisionAction(int num, boolean random, Predicate<AbstractCard> predicator, Consumer<AbstractCard> dowhat) {
        this.amount = num;
        this.random = random;
        this.predicator = predicator;
        this.dowhat = dowhat;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            if (cpr().discardPile.isEmpty() && cpr().drawPile.isEmpty()) {
                isDone = true;
                return;
            }
            if (!cpr().discardPile.isEmpty()) {
                addToTop(new ShuffleAction(cpr().drawPile, false));
                addToTop(new EmptyDeckShuffleAction());
            }
        }
        tickDuration();
        if (isDone) {
            addToBot(new TeleperceiveAction(amount, cpr().drawPile, random, predicator, dowhat));
        }
    }
}