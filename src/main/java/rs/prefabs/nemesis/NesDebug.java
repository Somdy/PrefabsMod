package rs.prefabs.nemesis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class NesDebug {
    private static Logger logger;
    private static final String MARK_LINE_START = "=====NES=====Debugging----->";
    private static final String MARK_LINE_END = "<-----Debugging=====NES=====";

    public static void Log(Object what) {
        Log(NesDebug.class, what);
    }
    
    public static void Log(@NotNull Object who, Object what) {
        logger = LogManager.getLogger(who.getClass().getName());
        logger.info(MARK_LINE_START + what + MARK_LINE_END);
    }
}