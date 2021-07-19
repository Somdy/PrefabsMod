package rs.prefabs.nemesis.character;

import basemod.BaseMod;
import rs.prefabs.nemesis.patches.NesCardEnum;
import rs.prefabs.nemesis.relics.*;

import java.util.ArrayList;
import java.util.List;

public final class NRP {
    private static final List<AbstractNesRelic> relics = new ArrayList<>();
    
    public static void AddRelics() {
        relics.add(new OldCenser());
        relics.add(new DecadentHomiliary());
        relics.add(new SearingHomiliary());
        relics.add(new OfferingHomiliary());
        relics.add(new BloodyHomiliary());
        
        relics.forEach(r -> BaseMod.addRelicToCustomPool(r.makeCopy(), NesCardEnum.Nemesis));
    }
}