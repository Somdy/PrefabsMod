package rs.prefabs.nemesis.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.character.NCP;
import rs.prefabs.nemesis.enums.NesRelicFamily;
import rs.prefabs.nemesis.patches.NesCardEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

public class OfferingHomiliary extends AbstractNesRelic {
    public static final String ID = NesFab.makeID("OfferingHomiliary");
    public static int OfferingAmt = 4;
    
    public OfferingHomiliary() {
        super(ID, NesRelicFamily.Homiliary, RelicTier.COMMON, LandingSound.CLINK);
        updateModifiedTips();
    }

    @Override
    public void updateModifiedTips() {
        description = DESCRIPTIONS[0];
        Matcher matcher = findRegex(description, AMT_REGEX, AMT_FLAG);
        if (matcher.find() && matcher.group(1) != null) {
            description = description.replace(matcher.group(1), String.valueOf(OfferingAmt));
        }
        resetOverlayInfo();
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        addToBot(new QuickAction().setAction(() -> {
            List<AbstractCard> tmp = new ArrayList<>(NCP.GetCards(c -> c.hasTag(NesCardEnum.OFFERING)));
            if (!tmp.isEmpty()) {
                for (int i = 0; i < OfferingAmt; i++) {
                    Optional<AbstractCard> card = getRandomCard(tmp, relicRng());
                    card.ifPresent(c -> {
                        c.isEthereal = true;
                        c.purgeOnUse = true;
                        addToTop(new MakeTempCardInDiscardAction(c, 1));
                    });
                }
            }
        }));
    }
}
