package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.NesDebug;
import rs.prefabs.nemesis.interfaces.AbstractOffering;

public class TaintedPotion extends AbstractNesGeneralCard implements AbstractOffering {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction(AbstractGameAction.ActionType.DEBUFF)
                .setAction(() -> {
                    for (int i = 0; i < ExMagicNum; i++) {
                        AbstractCreature target = findAnyTarget(false);
                        if (target != null) {
                            AbstractCreature source = isInEnemyUse() ? enemyUser : cpr();
                            addToBot(new VFXAction(new PotionBounceEffect(source.hb.cX, source.hb.cY, 
                                    target.hb.cX, target.hb.cY), 0.25F));
                            addToBot(new ApplyPowerAction(target, source, new WeakPower(target, magicNumber, isInEnemyUse())));
                            if (canTriggerEnchantedEffect()) {
                                NesDebug.Log(this, "Applying negative strength to " + target.name);
                                addToBot(new ApplyPowerAction(target, source, new StrengthPower(target, -2), -2));
                                addToBot(new ApplyPowerAction(target, source, new GainStrengthPower(target, 2), 2));
                            }
                        }
                    }
                }));
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
        addToBot(new QuickAction(AbstractGameAction.ActionType.DEBUFF)
                .setAction(() -> {
                    for (int i = 0; i < ExMagicNum; i++) {
                        AbstractCreature target = findAnyTarget(false);
                        if (target != null) {
                            AbstractCreature source = isInEnemyUse() ? enemyUser : cpr();
                            addToBot(new VFXAction(new PotionBounceEffect(source.hb.cX, source.hb.cY,
                                    target.hb.cX, target.hb.cY), 0.25F));
                            addToBot(new ApplyPowerAction(target, source, new WeakPower(target, magicNumber, isInEnemyUse())));
                            if (canTriggerEnchantedEffect()) {
                                NesDebug.Log(this, "Applying negative strength to " + target.name);
                                addToBot(new ApplyPowerAction(target, source, new StrengthPower(target, -1), -1));
                                addToBot(new ApplyPowerAction(target, source, new GainStrengthPower(target, 1), 1));
                            }
                        }
                    }
                }));
    }
}