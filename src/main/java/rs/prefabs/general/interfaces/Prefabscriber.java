package rs.prefabs.general.interfaces;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.utils.Debugger;

import java.util.ArrayList;

public class Prefabscriber {
    private static ArrayList<Prefaberface> toRemove;
    private static ArrayList<RegisterCustomCardAttrsSubscriber> registerCustomCardAttrsSubscribers;
    private static ArrayList<OnInitializeSubscriber> onInitializeSubscribers;
    private static ArrayList<PostTurnStartSubscriber> postTurnStartSubscribers;
    private static ArrayList<EndTurnPreDiscardSubscriber> endTurnPreDiscardSubscribers;
    private static ArrayList<MonsterEndTurnSubscriber> monsterEndTurnSubscribers;
    private static ArrayList<OnCardPlayedSubscriber> onCardPlayedSubscribers;
    private static ArrayList<OnPlayCardSubscriber> onPlayCardSubscribers;
    
    public static void initialize() {
        toRemove = new ArrayList<>();
        registerCustomCardAttrsSubscribers = new ArrayList<>();
        onInitializeSubscribers = new ArrayList<>();
        postTurnStartSubscribers = new ArrayList<>();
        endTurnPreDiscardSubscribers = new ArrayList<>();
        monsterEndTurnSubscribers = new ArrayList<>();
        onCardPlayedSubscribers = new ArrayList<>();
        onPlayCardSubscribers = new ArrayList<>();
    }

    public static void subscribe(Prefaberface sub) {
        subIfInstance(registerCustomCardAttrsSubscribers, sub, RegisterCustomCardAttrsSubscriber.class);
        subIfInstance(onInitializeSubscribers, sub, OnInitializeSubscriber.class);
        subIfInstance(postTurnStartSubscribers, sub, PostTurnStartSubscriber.class);
        subIfInstance(endTurnPreDiscardSubscribers, sub, EndTurnPreDiscardSubscriber.class);
        subIfInstance(monsterEndTurnSubscribers, sub, MonsterEndTurnSubscriber.class);
        subIfInstance(onCardPlayedSubscribers, sub, OnCardPlayedSubscriber.class);
        subIfInstance(onPlayCardSubscribers, sub, OnPlayCardSubscriber.class);
    }
    
    public static void unsubscribe(Prefaberface sub) {
        unsubIfInstance(registerCustomCardAttrsSubscribers, sub, RegisterCustomCardAttrsSubscriber.class);
        unsubIfInstance(onInitializeSubscribers, sub, OnInitializeSubscriber.class);
        unsubIfInstance(postTurnStartSubscribers, sub, PostTurnStartSubscriber.class);
        unsubIfInstance(endTurnPreDiscardSubscribers, sub, EndTurnPreDiscardSubscriber.class);
        unsubIfInstance(monsterEndTurnSubscribers, sub, MonsterEndTurnSubscriber.class);
        unsubIfInstance(onCardPlayedSubscribers, sub, OnCardPlayedSubscriber.class);
        unsubIfInstance(onPlayCardSubscribers, sub, OnPlayCardSubscriber.class);
    }

    private static <T> void subIfInstance(ArrayList<T> list, Prefaberface sub, @NotNull Class<T> clazz) {
        if (clazz.isInstance(sub)) {
            list.add(clazz.cast(sub));
        }
    }

    private static <T> void unsubIfInstance(ArrayList<T> list, Prefaberface sub, @NotNull Class<T> clazz) {
        if (clazz.isInstance(sub)) {
            list.remove(clazz.cast(sub));
        }
    }

    public static void publishPreAttrsAssign() {
        //Log("publish pre rarity assignment");
        for (RegisterCustomCardAttrsSubscriber sub : registerCustomCardAttrsSubscribers) {
            sub.receivePreAttrsAssign();
        }
    }
    
    public static void publishOnInitialize() {
        Log("publish post seed generated");
        for (OnInitializeSubscriber sub : onInitializeSubscribers) {
            sub.receiveOnInitialize();
        }
    }
    
    public static void publishPostTurnStart(AbstractCreature creature, boolean postDraw) {
        Log("publish post turn start, is player? " + creature.isPlayer);
        for (PostTurnStartSubscriber sub : postTurnStartSubscribers) {
            sub.receivePostTurnStart(creature, postDraw);
        }
    }
    
    public static void publishEndTurnPreDiscard() {
        Log("publish pre discarding");
        for (EndTurnPreDiscardSubscriber sub : endTurnPreDiscardSubscribers) {
            sub.receiveOnEndTurnPreDiscard();
        }
    }

    public static void publishMonsterTurnEnds(AbstractMonster m) {
        deLog("publish ending turn of " + m.name);
        for (MonsterEndTurnSubscriber sub : monsterEndTurnSubscribers) {
            sub.receiveOnMonsterTurnEnds(m);
        }
    }
    
    public static void publishCardPlayed(AbstractCard card, UseCardAction action) {
        Log("publish " + card.name + " used.");
        for (OnCardPlayedSubscriber sub : onCardPlayedSubscribers) {
            sub.receiveOnCardPlayed(card, action);
        }
    }

    public static void publishPlayingCard(AbstractCard card, AbstractCreature target, int energyOnUse) {
        Log("publish playing " + card.name + ".");
        for (OnPlayCardSubscriber sub : onPlayCardSubscribers) {
            sub.receiveOnPlayCard(card, target, energyOnUse);
        }
    }
    
    private static void deLog(Object what) {
        Debugger.deLog(Prefabscriber.class, what);
    }
    
    private static void Log(Object what) {
        Debugger.Log(Prefabscriber.class, what);
    }
}