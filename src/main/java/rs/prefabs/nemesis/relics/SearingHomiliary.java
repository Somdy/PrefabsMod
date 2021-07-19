package rs.prefabs.nemesis.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.enums.NesRelicFamily;
import rs.prefabs.nemesis.powers.HellfirePower;

import java.util.regex.Matcher;

public class SearingHomiliary extends AbstractNesRelic {
    public static final String ID = NesFab.makeID("SearingHomiliary");
    public static int HellfireAmt = 2;
    public static int ExtraAmt = 1;
    
    public SearingHomiliary() {
        super(ID, NesRelicFamily.Homiliary, RelicTier.UNCOMMON, LandingSound.CLINK);
        updateModifiedTips();
    }

    @Override
    public void updateModifiedTips() {
        String tmp = DESCRIPTIONS[0];
        Matcher matcher = findRegex(tmp, AMT_REGEX, AMT_FLAG);
        if (matcher.find() && matcher.group(1) != null) {
            if (matcher.group(3) != null && Integer.parseInt(matcher.group(3)) == 0)
                tmp = tmp.replace(matcher.group(1), String.valueOf(HellfireAmt));
            if (matcher.group(3) != null && Integer.parseInt(matcher.group(3)) == 1)
                tmp = tmp.replace(matcher.group(1), String.valueOf(ExtraAmt));
        }
        description = tmp;
        resetOverlayInfo();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!target.isDeadOrEscaped() && info.owner == cpr()) {
            flash();
            int applies = HellfireAmt;
            if (damageAmount > 0) applies += ExtraAmt;
            addToBot(new ApplyPowerAction(target, cpr(), new HellfirePower(target, cpr(), applies)));
        }
    }
}