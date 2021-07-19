package rs.prefabs.general.utils;

import rs.prefabs.general.data.CustomCardTags;
import rs.prefabs.general.data.CustomRarity;
import rs.prefabs.general.interfaces.Prefabscriber;
import rs.prefabs.general.listeners.ApplyPowerListener;
import rs.prefabs.general.listeners.TurnEventListener;
import rs.prefabs.general.listeners.UseCardListener;

public class Prefabinitializer {
    
    public static void preInit() {
        Prefabscriber.initialize();
        CustomRarity.initialize();
        CustomCardTags.initialize();
        AtkEffectMgr.initialize();
        UseCardListener.initialize();
        TurnEventListener.initialize();
        ApplyPowerListener.initialize();
    }
    
    public static void postInit() {
        
    }
}