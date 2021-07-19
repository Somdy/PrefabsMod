package rs.prefabs.nemesis.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NesPatterMgr {
    static final Pattern WitherNumPattern = Pattern.compile("\\b(?<=!)(?<!:)Wither(s)?(ed)?(Num)?(?=!)", Pattern.CASE_INSENSITIVE);

    public static String useWitherPattern(String origin, String replacement) {
        final Matcher m = WitherNumPattern.matcher(origin);
        if (m.find())
            return origin.replace(m.group(0), replacement);
        return origin;
    }
}