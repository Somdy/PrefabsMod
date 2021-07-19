package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.powers.HellfirePower;

public class GateOfFire extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction(AbstractGameAction.ActionType.DEBUFF)
                .setAction(() -> {
                    for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                        if (m != null && !m.isDeadOrEscaped()) {
                            addToBot(new ApplyPowerAction(m, s, new HellfirePower(m, s, magicNumber)));
                        }
                    }
                })
                .setFollowupAction(upgraded ? new DelayAction(() -> addToBot(new QuickAction(AbstractGameAction.ActionType.BLOCK)
                        .setAction(() -> AbstractDungeon.getMonsters().monsters.stream()
                                .filter(m -> m != null && !m.isDeadOrEscaped() && m.hasPower(HellfirePower.POWER_ID))
                                .forEach(m -> {
                                    int sum = m.powers.stream().filter(p -> p.ID.equals(HellfirePower.POWER_ID))
                                            .mapToInt(p -> p.amount).sum();
                                    if (sum > 0)
                                        addToBot(new GainBlockAction(s, m, sum));
                                }
                    )))) : null));
        if (canTriggerEnchantedEffect()) {
            addToBot(new QuickAction(AbstractGameAction.ActionType.BLOCK)
                    .setAction(() -> {
                        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                            if (m != null && !m.isDeadOrEscaped() && m.currentBlock > 0)
                                addToBot(new LoseBlockAction(m, s, ExMagicNum));
                        }
                    }));
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
}