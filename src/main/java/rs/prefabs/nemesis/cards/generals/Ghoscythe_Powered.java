package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.powers.PsychicPower;

import java.util.List;

public class Ghoscythe_Powered extends AbstractNesGeneralCard {
    private static final int DRAW = 2;
    private static final int BUFFER = 1;
    private static final int WEAK = 2;

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction(AbstractGameAction.ActionType.DAMAGE)
                .setAction(() -> {
                    List<AbstractCreature> tmp;
                    tmp = isInEnemyUse() ?
                            getAllExptCreatures(c -> c.isPlayer)
                            : getAllExptCreatures(c -> !c.isPlayer && !c.isDeadOrEscaped());
                    final int slot = cardRandomRng().random(0, 3);
                    boolean skipEffect = false;
                    if (!tmp.isEmpty()) {
                        for (AbstractCreature c : tmp) {
                            addToBot(new NullableSrcDamageAction(c, crtDmgInfo(s, damage, damageTypeForTurn), 
                                    getRandom(objsToList(AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                                            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                                            AbstractGameAction.AttackEffect.SLASH_VERTICAL))
                                            .orElse(AbstractGameAction.AttackEffect.SLASH_HEAVY)));
                            if (slot == 3 && onlyHasBuff(s)) {
                                skipEffect = true;
                                addToBot(new ApplyPowerAction(c, s, new WeakPower(c, WEAK, isInEnemyUse())));
                                if (canTriggerEnchantedEffect())
                                    addToBot(new ApplyPowerAction(c, s, new WeakPower(c, WEAK, isInEnemyUse())));
                            }
                        }
                    }
                    if (!skipEffect) {
                        extraEffect(slot, s);
                        if (canTriggerEnchantedEffect())
                            extraEffect(slot, s);
                    }
                }));
    }
    
    private void extraEffect(int slot, AbstractCreature s) {
        if (!onlyHasBuff(s)) return;
        switch (slot) {
            case 0:
                if (s.isPlayer)
                    addToBot(new DrawCardAction(s, DRAW));
                break;
            case 1:
                addToBot(new ApplyPowerAction(s, s, new PsychicPower(s, magicNumber)));
                break;
            case 2:
                addToBot(new ApplyPowerAction(s, s, new BufferPower(s, BUFFER)));
                break;
        }
    }
    
    private boolean onlyHasBuff(AbstractCreature s) {
        return s.powers.stream().allMatch(p -> isPowerTypeOf(p, AbstractPower.PowerType.BUFF));
    }

    @Override
    public void promote() {
        if (canUpgrade()) {
            super.promote();
            addTip(MSG[0], MSG[1]);
        }
    }

    @Override
    protected void spectralize() {
        upgradeBaseCost(2);
    }

    @Override
    protected void despectralize() {
        upgradeNewCost();
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}