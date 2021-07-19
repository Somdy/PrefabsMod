package rs.prefabs.general.listeners;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class TurnEventListener {
    private static Map<AbstractCreature, Integer> crtTurnCounter;
    private static Map<AbstractCreature, Predicate<AbstractCreature>> timeFrozen;

    public static final int[] lastTurn;
    public static final int[] currTurn;
    
    static {
        lastTurn = new int[] {0};
        currTurn = new int[] {0};
    }
    
    public static void initialize() {
        crtTurnCounter = new HashMap<>();
        timeFrozen = new HashMap<>();
    }
    
    public static void loadAtStartOfCombat(AbstractRoom r) {
        for (AbstractMonster m : r.monsters.monsters) {
            if (!m.isDeadOrEscaped())
                crtTurnCounter.put(m, 0);
        }
        if (AbstractDungeon.player != null && !AbstractDungeon.player.isDeadOrEscaped())
            crtTurnCounter.put(AbstractDungeon.player, 0);
    }
    
    public static void triggerEndOfTurnEvents(AbstractCreature creature) {
        checkIfShouldIncrsCreaturnTurnCounters(creature);
        lastTurn[0] = GameActionManager.turn;
    }
    
    public static void triggerStartOfTurnEvents(@NotNull AbstractCreature creature) {
        if (creature.isPlayer) {
            currTurn[0] = GameActionManager.turn;
        }
    }
    
    public static int getPassingTurnsOf(AbstractCreature creature) {
        return crtTurnCounter.containsKey(creature) ? crtTurnCounter.get(creature) : lastTurn[0];
    }
    
    private static void checkIfShouldIncrsCreaturnTurnCounters(@NotNull AbstractCreature creature) {
        if (creature.isDeadOrEscaped() || isTimeFrozen(creature)) return;
        if (!crtTurnCounter.containsKey(creature)) {
            crtTurnCounter.put(creature, 1);
            return;
        }
        crtTurnCounter.replace(creature, crtTurnCounter.get(creature) + 1);
    }
    
    public static boolean isTimeFrozen(AbstractCreature creature) {
        return timeFrozen.containsKey(creature) && timeFrozen.get(creature).test(creature);
    }
    
    public static void setCreatureTimeFrozen(boolean forcedReplaced, AbstractCreature creature, Predicate<AbstractCreature> frozen) {
        if (timeFrozen.containsKey(creature) && forcedReplaced)
            timeFrozen.put(creature, frozen);
        else 
            timeFrozen.putIfAbsent(creature, frozen);
    }
}