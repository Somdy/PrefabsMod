package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.nemesis.powers.DecayPower;
import rs.prefabs.nemesis.powers.ParalyticPower;

public class MindRot extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (!isInEnemyUse()) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(m, s, new DecayPower(m, ExMagicNum), ExMagicNum));
                    if (canTriggerEnchantedEffect())
                        addToBot(new ApplyPowerAction(m, s, new ParalyticPower(m, magicNumber), magicNumber));
                }
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
}