package rs.prefabs.nemesis.utils;

import rs.prefabs.nemesis.IDs.CardID;
import rs.prefabs.nemesis.character.NCP;
import rs.prefabs.nemesis.data.NesDataReader;

public class Nesinitializer {

    public static void preInit() {
        CardID.initialize();
        SwitchMgr.initialize();
        NesDataReader.initialize();
        NCP.initialize();
    }

    public static void postInit() {
        NesImageMst.initialize();
    }
}