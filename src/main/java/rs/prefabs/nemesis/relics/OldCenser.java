package rs.prefabs.nemesis.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.powers.PsychicPower;

public class OldCenser extends AbstractNesRelic {
    public static final String ID = NesFab.makeID("OldCenser");
    
    public OldCenser() {
        super(ID, RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        if (usedUp) {
            usedUp = false;
            grayscale = false;
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != cpr() && damageAmount > 0 && !usedUp) {
            usedUp = true;
            grayscale = true;
            addToBot(new RelicAboveCreatureAction(cpr(), this));
            addToBot(new ApplyPowerAction(cpr(), cpr(), new PsychicPower(cpr(), 5), 5));
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}