package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.nemesis.interfaces.UndeadOffering;
import rs.prefabs.nemesis.powers.DecayPower;

public class CripplingFears extends AbstractNesGeneralCard implements UndeadOffering {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        for (AbstractCreature m : getAllExptCreatures(c -> c != cpr() && !c.isDeadOrEscaped())) {
            for (int i = 0; i < ExMagicNum; i++) {
                addToBot(new ApplyPowerAction(m, s, new DecayPower(m, magicNumber)));
            }
        }
        if (canTriggerEnchantedEffect()) {
            for (AbstractCreature c : getAllExptCreatures(c -> c.currentBlock > 0)) {
                addToBot(new LoseBlockAction(c, s, c.currentBlock));
            }
        }
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }

    @Override
    public void triggerOnInitializationAndCompletion() {
        for (AbstractCreature m : getAllExptCreatures(c -> c != cpr() && !c.isDeadOrEscaped())) {
            for (int i = 0; i < ExMagicNum; i++) {
                addToBot(new ApplyPowerAction(m, cpr(), new DecayPower(m, magicNumber)));
            }
        }
        if (canTriggerEnchantedEffect()) {
            for (AbstractCreature c : getAllExptCreatures(c -> c.currentBlock > 0)) {
                addToBot(new LoseBlockAction(c, cpr(), c.currentBlock));
            }
        }
    }

    @Override
    public void triggerUndeadEffect() {
        addToBot(new MakeTempCardInDrawPileAction(this.makeCopy(), 1, true, true));
        addToBot(new DelayAction(() -> {
            for (AbstractCard c : cpr().drawPile.group) {
                c.baseDamage = MathUtils.ceil(c.baseDamage / 2F);
                c.baseBlock = MathUtils.ceil(c.baseBlock / 2F);
                c.isDamageModified = c.baseDamage != c.damage;
                c.isBlockModified = c.baseBlock != c.block;
                c.applyPowers();
                c.superFlash(Color.SCARLET.cpy());
            }
        }));
    }
}