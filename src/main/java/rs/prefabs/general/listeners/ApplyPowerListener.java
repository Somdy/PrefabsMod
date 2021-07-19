package rs.prefabs.general.listeners;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rs.prefabs.general.listeners.utils.PowerMplr;
import rs.prefabs.general.interfaces.TripleUniMap;
import rs.prefabs.general.utils.Debugger;

import java.util.*;
import java.util.function.Predicate;

public class ApplyPowerListener {
    private static List<CustomPowerMplr> manipulators;
    
    public static void initialize() {
        manipulators = new ArrayList<>();
    }
    
    public static void addNewManipulator(int ID, int Rank, Predicate<PowerMplr> effective, PowerMplr mplr) {
        if (!registerMplrWithAbsenceCheck(ID, Rank, effective, mplr))
            Debugger.Log(ApplyPowerListener.class, 
                    "PowerMplr-" + ID + " failed to register in. It will not have any effects.");
    }
    
    public static boolean removeManipulator(int ID) {
        if (contains(ID)) {
            manipulators.removeIf(m -> m.getID() == ID);
            Debugger.deLog(ApplyPowerListener.class, "Removing PowerMplr-"  + ID + " from listener list.");
        }
        return manipulators.stream().noneMatch(m -> m.getID() == ID);
    }
    
    private static boolean registerMplrWithAbsenceCheck(int ID, int Rank, Predicate<PowerMplr> effective, PowerMplr mplr) {
        if (contains(ID)) {
            boolean shouldReplace = getMplr(ID).getRank() > Rank;
            if (shouldReplace) {
                int index = manipulators.indexOf(getMplr(ID));
                manipulators.removeIf(m -> m.getID() == ID);
                if (index < 0) {
                    Debugger.deLog(ApplyPowerListener.class, "Missing old PowerMplr-" + ID + ", adding a new one.");
                    manipulators.add(new CustomPowerMplr(ID, Rank, effective, mplr));
                } else {
                    manipulators.set(index, new CustomPowerMplr(ID, Rank, effective, mplr));
                    Debugger.Log(ApplyPowerListener.class, "PowerMplr-" + ID + " has been replaced by a new one.");
                }
            }
        } else {
            manipulators.add(new CustomPowerMplr(ID, Rank, effective, mplr));
            Debugger.Log("PowerMplr-" + ID + " has been applied");
        }
        return true;
    }
    
    private static CustomPowerMplr getMplr(int ID) {
        return manipulators.stream().filter(m -> m.RankID.containsKey(ID)).findFirst().orElse(null);
    }
    
    private static boolean contains(int ID) {
        if (manipulators == null || manipulators.isEmpty())
            return false;
        for (CustomPowerMplr mplr : manipulators) {
            return mplr.getID() == ID;
        }
        return false;
    }
    
    public static void clearPostCombat() {
        manipulators.clear();
    }
    
    public static AbstractPower onApplyPower(@NotNull AbstractPower power, AbstractCreature target, AbstractCreature source) {
        String Power_ID = power.ID;
        if (!manipulators.isEmpty()) {
            List<PowerMplr> tmp = new ArrayList<>();
            manipulators.stream().filter(m -> m.effective.test(m.mplr)).forEach(m -> {
                if (!tmp.contains(m.mplr))
                    tmp.add(m.mplr);
            });
            if (!tmp.isEmpty()) {
                for (PowerMplr mplr : tmp) {
                    power = mplr.manipulate(power, target, source);
                    if (power == null || power.amount == 0) {
                        Debugger.deLog(ApplyPowerListener.class, Power_ID + " was cancled.");
                        break;
                    }
                }
            }
        }
        return power;
    }
    
    private static class CustomPowerMplr implements TripleUniMap<PowerMplr, Map<Integer, Integer>, Predicate<PowerMplr>> {
        PowerMplr mplr;
        Map<Integer, Integer> RankID;
        Predicate<PowerMplr> effective;
        private final int ID;
        private final int Rank;
        
        public CustomPowerMplr(int ID, int Rank, Predicate<PowerMplr> effective, PowerMplr mplr) {
            this.ID = ID;
            this.Rank = Rank;
            RankID = new HashMap<>();
            RankID.put(ID, Rank);
            this.effective = effective;
            this.mplr = mplr;
        }

        @Override
        public Map<Integer, Integer> accept(PowerMplr powerMplr) {
            if (identify(powerMplr))
                return RankID;
            return null;
        }

        @Override
        public Predicate<PowerMplr> find(PowerMplr powerMplr) {
            if (identify(powerMplr))
                return effective;
            return null;
        }
        
        @Nullable
        @Contract(pure = true)
        public CustomPowerMplr get(Integer ID) {
            return this.ID == ID ? this : null;
        }

        public int getID() {
            return ID;
        }

        public int getRank() {
            return Rank;
        }

        @Override
        public boolean identify(PowerMplr powerMplr) {
            return this.mplr == powerMplr;
        }
    }
}