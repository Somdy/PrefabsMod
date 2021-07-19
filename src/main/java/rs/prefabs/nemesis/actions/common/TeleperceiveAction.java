package rs.prefabs.nemesis.actions.common;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.actions.common.SimpleGridCardSelectBuilder;
import rs.prefabs.general.tools.GridCardManipulator;
import rs.prefabs.nemesis.NesDebug;
import rs.prefabs.nemesis.NesFab;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class TeleperceiveAction extends AbstractPrefabGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(NesFab.makeID("TeleperceiveAction"));
    public static final String[] TEXT = uiStrings.TEXT;
    private CardGroup where;
    private boolean random;
    private Predicate<AbstractCard> predicator;
    private Consumer<AbstractCard> sideEffect;
    private String msg;
    private boolean canCancel;
    private boolean anyNumber;
    
    public TeleperceiveAction(int amount, CardGroup where, boolean random, Predicate<AbstractCard> predicator, 
                              Consumer<AbstractCard> sideEffect) {
        this.amount = amount;
        this.where = where;
        this.random = random;
        this.predicator = predicator;
        this.sideEffect = sideEffect;
        this.msg = null;
        this.canCancel = true;
        this.anyNumber = true;
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }
    
    public TeleperceiveAction(int amount, CardGroup where, boolean random, Predicate<AbstractCard> predicator) {
        this(amount, where, random, predicator, null);
    }
    
    public TeleperceiveAction(int amount, CardGroup where, boolean random) {
        this(amount, where, random, c -> true, null);
    }
    
    public TeleperceiveAction setPredicator(Predicate<AbstractCard> predicator) {
        this.predicator = predicator;
        return this;
    }
    
    public TeleperceiveAction setSideEffect(Consumer<AbstractCard> sideEffect) {
        this.sideEffect = sideEffect;
        return this;
    }
    
    public TeleperceiveAction setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    
    public TeleperceiveAction setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
        return this;
    }
    
    public TeleperceiveAction setAnyNumber(boolean anyNumber) {
        this.anyNumber = anyNumber;
        return this;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            if (where.isEmpty() || predicator == null) {
                isDone = true;
                return;
            }
            int count = countSpecificCards(where, predicator);
            if (count < amount)
                amount = count;
            NesDebug.Log(this, "Detecting only " + amount + " cards meet the reqs in " + where.type);
            if (amount == 0) {
                isDone = true;
                executeWhenJobsDone();
                return;
            }
            if (random) {
                CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                int size = 0;
                for (AbstractCard card : where.group) {
                    if (predicator.test(card) && !tmp.contains(card)) {
                        tmp.addToRandomSpot(card);
                        size++;
                        if (size == amount) break;
                    }
                }
                if (tmp.size() - 1 != amount)
                    NesDebug.Log(this, "Possible bug exists: getting only " + (tmp.size() - 1) + " cards meeting reqs");
                if (!tmp.isEmpty()) {
                    for (AbstractCard card : tmp.group) {
                        if (where.contains(card) && cpr().hand.size() < BaseMod.MAX_HAND_SIZE) {
                            if (sideEffect != null)
                                sideEffect.accept(card);
                            where.moveToHand(card);
                        }
                    }
                }
            } else {
                addToBot(new SimpleGridCardSelectBuilder(predicator)
                        .setAmount(amount)
                        .setCardGroup(where)
                        .setAnyNumber(anyNumber)
                        .setCanCancel(canCancel)
                        .setShouldMatchAll(true)
                        .setDisplayInOrder(false)
                        .setMsg(TEXT[0] + (anyNumber ? TEXT[1]: TEXT[2]) + amount + TEXT[3]  + (msg == null ? "" : msg))
                        .setManipulator(new GridCardManipulator() {
                            @Override
                            public boolean manipulate(AbstractCard card, int indexOfCard, CardGroup group) {
                                if (cpr().hand.size() < BaseMod.MAX_HAND_SIZE) {
                                    if (sideEffect != null)
                                        sideEffect.accept(card);
                                    where.moveToHand(card);
                                }
                                return false;
                            }
                        })
                );
            }
            isDone = true;
            executeWhenJobsDone();
        }
    }
}