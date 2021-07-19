package rs.prefabs.nemesis.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import rs.prefabs.general.abstracts.AbstractPrefabRelic;
import rs.prefabs.general.misc.SubPrefabClass;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.enums.NesRelicFamily;
import rs.prefabs.nemesis.utils.NesSK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbstractNesRelic extends AbstractPrefabRelic {
    protected static final String AMT_REGEX = "\\s#[rybgp](\\[amount(s)?_(\\d)])([%])?\\s";
    protected static final int AMT_FLAG = Pattern.MULTILINE | Pattern.CASE_INSENSITIVE;
    
    public NesRelicFamily family;
    
    public AbstractNesRelic(String id, NesRelicFamily family, RelicTier tier, LandingSound sfx) {
        super(id, ImageMaster.loadImage(NesSK.GetNesRelicImage(NesFab.rmPrefix(id))), 
                ImageMaster.loadImage(NesSK.GetNesRelicOutline(NesFab.rmPrefix(id))),
                tier, sfx);
        this.family = family;
        setPrefabType(clazz.create(new SubPrefabClass<AbstractNesRelic>()));
    }
    
    public AbstractNesRelic(String id, RelicTier tier, LandingSound sfx) {
        this(id, NesRelicFamily.None, tier, sfx);
    }
    
    protected Matcher findRegex(String target, String regex, int flags) {
        Pattern pattern = Pattern.compile(regex, flags);
        return pattern.matcher(target);
    }
    
    public void updateModifiedTips() {}
}