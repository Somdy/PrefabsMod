package rs.prefabs.general.actions.utility;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.tools.GridCardManipulator;
import rs.prefabs.general.utils.Debugger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SimpleGridCardSelectBuilder extends AbstractPrefabGameAction {
    private String msg;
    private GridCardManipulator cm;
    private boolean shouldMatchAll;
    private CardGroup[] cardGroups;
    private CardGroup tmpGroup;
    private Predicate<AbstractCard>[] predicators;
    private List<AbstractCard> removeList;
    private boolean anyNumber;
    private boolean canCancel;
    private boolean forUpgrade;
    private boolean forTransform;
    private boolean forPurge;
    private boolean displayInOrder;

    @SafeVarargs
    public SimpleGridCardSelectBuilder(String msg, GridCardManipulator cm, boolean shouldMatchAll, boolean anyNumber, boolean canCancel,
                                       boolean forUpgrade, boolean forTransform, boolean forPurge, Predicate<AbstractCard>... predicators) {
        this.msg = msg;
        this.cm = cm;
        this.shouldMatchAll = shouldMatchAll;
        this.anyNumber = anyNumber;
        this.canCancel = canCancel;
        this.forUpgrade = forUpgrade;
        this.forTransform = forTransform;
        this.forPurge = forPurge;
        this.predicators = predicators;
        tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        removeList = new ArrayList<>();
        displayInOrder = false;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }

    @SafeVarargs
    public SimpleGridCardSelectBuilder(String msg, GridCardManipulator cm, boolean shouldMatchAll, boolean anyNumber, boolean canCancel,
                                       Predicate<AbstractCard>... predicators) {
        this(msg, cm, shouldMatchAll, anyNumber, canCancel, false, false, false, predicators);
    }

    @SafeVarargs
    public SimpleGridCardSelectBuilder(GridCardManipulator cm, Predicate<AbstractCard>... predicators) {
        this(null, cm, false, false, false, false, false, false, predicators);
    }

    @SafeVarargs
    public SimpleGridCardSelectBuilder(Predicate<AbstractCard>... predicators) {
        this(null, null, false, false, false, false, false, false, predicators);
    }

    public SimpleGridCardSelectBuilder setCardGroup(CardGroup... cardGroups) {
        this.cardGroups = cardGroups;
        return this;
    }

    public SimpleGridCardSelectBuilder setDisplayInOrder(boolean displayInOrder) {
        this.displayInOrder = displayInOrder;
        return this;
    }

    public SimpleGridCardSelectBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public SimpleGridCardSelectBuilder setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public SimpleGridCardSelectBuilder setManipulator(GridCardManipulator cm) {
        this.cm = cm;
        return this;
    }

    public SimpleGridCardSelectBuilder setAnyNumber(boolean anyNumber) {
        this.anyNumber = anyNumber;
        return this;
    }

    public SimpleGridCardSelectBuilder setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
        return this;
    }

    public SimpleGridCardSelectBuilder setShouldMatchAll(boolean shouldMatchAll) {
        this.shouldMatchAll = shouldMatchAll;
        return this;
    }

    public SimpleGridCardSelectBuilder setForUpgrade(boolean forUpgrade) {
        this.forUpgrade = forUpgrade;
        return this;
    }

    public SimpleGridCardSelectBuilder setForTransform(boolean forTransform) {
        this.forTransform = forTransform;
        return this;
    }

    public SimpleGridCardSelectBuilder setForPurge(boolean forPurge) {
        this.forPurge = forPurge;
        return this;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            if (cardGroups == null) {
                isDone = true;
                return;
            }
            tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (int i = 0; i < cardGroups.length; i++) {
                if (cardGroups[i].isEmpty()) continue;
                Debugger.Log("Adding cards in group " + i + " to candicates list");
                for (AbstractCard card : cardGroups[i].group) {
                    if (displayInOrder) tmpGroup.addToBottom(card);
                    else tmpGroup.addToRandomSpot(card);
                }
            }
            Debugger.Log("Judging if the cards " + (shouldMatchAll ? " match all conditions" : " match any condition."));
            List<AbstractCard> removeList = new ArrayList<>();
            for (AbstractCard card : tmpGroup.group) {
                int unmatches = 0;
                boolean anyMatched = false;
                for (Predicate<AbstractCard> predicator : predicators) {
                    if (!predicator.test(card))
                        unmatches++;
                    else if (predicator.test(card))
                        anyMatched = true;
                }
                if (shouldMatchAll && unmatches > 0)
                    removeList.add(card);
                    //tmpGroup.removeCard(card);
                else if (unmatches > predicators.length)
                    removeList.add(card);
                    //tmpGroup.removeCard(card);
                else if (anyMatched)
                    continue;
            }
            for (AbstractCard card : removeList) {
                if (tmpGroup.contains(card))
                    tmpGroup.removeCard(card);
            }
            if (tmpGroup.isEmpty()) {
                Debugger.Log("No cards matched???");
                isDone = true;
                return;
            }
            if (anyNumber) {
                AbstractDungeon.gridSelectScreen.open(tmpGroup, amount, true, msg);
            } else {
                AbstractDungeon.gridSelectScreen.open(tmpGroup, amount, msg, forUpgrade, forTransform, canCancel, forPurge);
            }
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            int index = 0;
            for (AbstractCard card : tmpGroup.group) {
                if (AbstractDungeon.gridSelectScreen.selectedCards.contains(card)) {
                    if (cm.manipulate(card, index, tmpGroup)) {

                    }
                    index++;
                }
                cpr().hand.refreshHandLayout();
                cpr().hand.applyPowers();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            cpr().hand.refreshHandLayout();
        }
        tickDuration();
        if (isDone) {
            tmpGroup.clear();
        }
    }
}