package rs.prefabs.nemesis.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.listeners.ApplyPowerListener;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.enums.NesRelicFamily;
import rs.prefabs.nemesis.powers.DecayPower;

import java.util.regex.Matcher;

public class DecadentHomiliary extends AbstractNesRelic {
    public static final String ID = NesFab.makeID("DecadentHomiliary");
    public static int DecayAmt = 2;
    
    public DecadentHomiliary() {
        super(ID, NesRelicFamily.Homiliary, RelicTier.COMMON, LandingSound.CLINK);
        updateModifiedTips();
    }

    @Override
    public void updateModifiedTips() {
        description = DESCRIPTIONS[0];
        Matcher matcher = findRegex(description, AMT_REGEX, AMT_FLAG);
        if (matcher.find() && matcher.group(1) != null) {
            description = description.replace(matcher.group(1), String.valueOf(DecayAmt));
        }
        resetOverlayInfo();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        ApplyPowerListener.addNewManipulator((ID.length() + ascenLevel()) * currFloor(), 1, pm -> cpr().hasRelic(ID),
                (pow, tgt, src) -> {
                    if (src == cpr() && !tgt.isDeadOrEscaped() && isPowerTypeOf(pow, AbstractPower.PowerType.DEBUFF)
                            && !(pow instanceof DecayPower)) {
                        addToBot(new RelicAboveCreatureAction(tgt, this));
                        addToBot(new ApplyPowerAction(tgt, src, new DecayPower(tgt, DecayAmt)));
                    }
                    return pow;
                });
    }
}