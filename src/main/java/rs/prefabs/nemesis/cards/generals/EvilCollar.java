package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.listeners.ApplyPowerListener;
import rs.prefabs.nemesis.actions.common.TeleperceiveAction;
import rs.prefabs.nemesis.character.OfferHelper;
import rs.prefabs.nemesis.interfaces.TotemOffering;

public class EvilCollar extends AbstractNesGeneralCard implements TotemOffering {
    private static int rank;
    
    public EvilCollar() {
        super();
        rank = 0;
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new ApplyPowerAction(s, s, new StrengthPower(s, ExMagicNum)));
        addToBot(new ApplyPowerAction(s, s, new DexterityPower(s, -ExMagicNum)));
        if (canTriggerEnchantedEffect()) {
            if (!isInEnemyUse()) {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != null && !m.isDeadOrEscaped())
                        addToBot(new ApplyPowerAction(m, s, new StrengthPower(m, -1)));
                }
            } else {
                addToBot(new ApplyPowerAction(cpr(), enemyUser, new StrengthPower(cpr(), -1)));
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
    public void triggerOnInitializationAndCompletion() {
        addToBot(new ApplyPowerAction(cpr(), cpr(), new StrengthPower(cpr(), ExMagicNum)));
        addToBot(new ApplyPowerAction(cpr(), cpr(), new DexterityPower(cpr(), -ExMagicNum)));
        if (canTriggerEnchantedEffect()) {
            if (!isInEnemyUse()) {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != null && !m.isDeadOrEscaped())
                        addToBot(new ApplyPowerAction(m, cpr(), new StrengthPower(m, -1)));
                }
            } else {
                addToBot(new ApplyPowerAction(cpr(), enemyUser, new StrengthPower(cpr(), -1)));
            }
        }
    }

    @Override
    public void onOfferInitialized() {
        addToBot(new QuickAction().setAction(() -> {
            ApplyPowerListener.addNewManipulator(getPrefabid() * "EvilCollar".length() + 1, 
                    rank, pm -> OfferHelper.HasOffering(this),
                    (pow, tgt, src) -> {
                        if (pow != null && tgt == cpr() && isPowerTypeOf(pow, AbstractPower.PowerType.DEBUFF) && pow.amount != 0) {
                            flash();
                            if (!upgraded) {
                                addToBot(new VFXAction(new OfferingEffect()));
                                addToBot(new LoseHPAction(cpr(), cpr(), magicNumber, AbstractGameAction.AttackEffect.NONE));
                            }
                            addToBot(new TeleperceiveAction(magicNumber, cpr().drawPile, true,
                                    c -> isCardTypeOf(c, CardType.SKILL)));
                        }
                        return pow;
                    });
            rank++;
        }));
    }

    @Override
    public void onOfferCompleted() {
        if (!ApplyPowerListener.removeManipulator(getPrefabid() * "EvilCollar".length() + 1))
            Log("Failed to remove " + (getPrefabid() * "EvilCollar".length() + 1) + "-PowerMplr. It may be still working.");
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}