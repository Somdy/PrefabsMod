package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import rs.prefabs.general.actions.common.DamageAndDoWhenUnblocked;
import rs.prefabs.nemesis.interfaces.AbstractOffering;
import rs.prefabs.nemesis.patches.NesCustomEnum;

public class FoulBlood extends AbstractNesGeneralCard implements AbstractOffering {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new DamageAndDoWhenUnblocked(t, crtDmgInfo(s, damage, damageTypeForTurn),
                NesCustomEnum.NES_PLAGUE, m -> addToBot(new ApplyPowerAction(m, s, 
                new VulnerablePower(m, magicNumber, isInEnemyUse()), magicNumber))));
    }

    @Override
    protected void spectralize() {
        enchantMagics(0, baseMagicNumber + 1);
    }

    @Override
    protected void despectralize() {
        disenchantMagics(0);
    }

    @Override
    public void triggerOnInitializationAndCompletion() {
        AbstractCreature target = findAnyTarget(false);
        if (target != null) {
            addToBot(new DamageAndDoWhenUnblocked(target, crtDmgInfo(null, damage, damageTypeForTurn),
                    NesCustomEnum.NES_PLAGUE, m -> addToBot(new ApplyPowerAction(m, isInEnemyUse() ? enemyUser : cpr(),
                    new VulnerablePower(m, magicNumber, isInEnemyUse()), magicNumber))));
        }
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}