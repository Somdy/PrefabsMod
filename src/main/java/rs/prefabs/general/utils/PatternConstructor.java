package rs.prefabs.general.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternConstructor {
    private final String regex;
    private Pattern pattern;
    
    public PatternConstructor(String regex) {
        this.regex = regex;
        pattern = null;
    }

    public PatternConstructor compile() {
        pattern = Pattern.compile(regex);
        return this;
    }
    
    public PatternConstructor compile(int flags) {
        pattern = Pattern.compile(regex, flags);
        return this;
    }
    
    public Matcher match(String str, int flags) {
        pattern = Pattern.compile(regex, flags);
        return match(str);
    }
    
    public Matcher match(String str) {
        return pattern.matcher(str);
    }
    
    public String matchAndReplace(String str, String replacement) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            str = str.replace(matcher.group(0), replacement);
        }
        return str;
    }
}