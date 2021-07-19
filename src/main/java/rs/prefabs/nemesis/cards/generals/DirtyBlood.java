package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.prefabs.general.actions.common.DamageAndDoWhenUnblocked;
import rs.prefabs.nemesis.interfaces.AbstractOffering;
import rs.prefabs.nemesis.patches.NesCustomEnum;
import rs.prefabs.nemesis.powers.AdvancedVulnerablePower;

public class DirtyBlood extends AbstractNesGeneralCard implements AbstractOffering {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (!isInEnemyUse()) {
            for (AbstractMonster m : getAllLivingMstrs()) {
                addToBot(new DamageAndDoWhenUnblocked(m, crtDmgInfo(s, damage, damageTypeForTurn), NesCustomEnum.NES_PLAGUE,
                        crt -> {
                    addToBot(new ApplyPowerAction(m, s, new AdvancedVulnerablePower(m, upgraded ? 0.75F : 0.65F, magicNumber)));
                    if (canTriggerEnchantedEffect())
                        addToBot(new ApplyPowerAction(m, s, new WeakPower(m, ExMagicNum, false)));
                        }));
            }
            return;
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
    public void triggerOnInitializationAndCompletion() {
        for (AbstractMonster m : getAllLivingMstrs()) {
            addToBot(new DamageAndDoWhenUnblocked(m, crtDmgInfo(cpr(), damage, damageTypeForTurn), NesCustomEnum.NES_PLAGUE,
                    crt -> {
                        addToBot(new ApplyPowerAction(m, cpr(), new AdvancedVulnerablePower(m, upgraded ? 0.75F : 0.65F, 
                                magicNumber)));
                        if (canTriggerEnchantedEffect())
                            addToBot(new ApplyPowerAction(m, cpr(), new WeakPower(m, ExMagicNum, false)));
                    }));
        }
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}