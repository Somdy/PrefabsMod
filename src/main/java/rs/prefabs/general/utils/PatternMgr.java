package rs.prefabs.general.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PatternMgr {
    static final Pattern ExMagicPattern = Pattern.compile("\\b(?<=!)(?<!:)ExMagic(s)?(Num)?(?=!)", Pattern.CASE_INSENSITIVE);
    
    public static String useExMagicPattern(String origin, String replacement) {
        final Matcher m = ExMagicPattern.matcher(origin);
        if (m.find())
            return origin.replace(m.group(0), replacement);
        return origin;
    }
}