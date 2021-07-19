package rs.prefabs.general.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class Debugger {
    private static Logger logger;
    private static final String MARK_LINE_START = "====PREFAB---->";
    private static final String MARK_LINE_END = "<----PREFAB====";

    public static void Log(Object what) {
        Log(Debugger.class, what);
    }

    public static void Log(@NotNull Object who, Object what) {
        logger = LogManager.getLogger(who.getClass().getName());
        logger.info(MARK_LINE_START + what + MARK_LINE_END);
    }

    public static void deLog(@NotNull Object who, Object what) {
        logger = LogManager.getLogger(who.getClass().getName());
        logger.info(MARK_LINE_START + what);
    }
}