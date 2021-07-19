package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ReaperEffect;
import rs.prefabs.general.actions.common.*;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.listeners.ApplyPowerListener;
import rs.prefabs.general.listeners.TurnEventListener;
import rs.prefabs.nemesis.cards.NesOptionCard;

public class CorruptorDisguise extends AbstractNesGeneralCard {
    private static int rank;
    private int counter;
    
    public CorruptorDisguise() {
        super();
        rank = 0;
        counter = 0;
    }
    
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction().setAction(() -> {
            NesOptionCard dmg = NesOptionCard.createOption(MSG[upgraded ? 4 : 0], MSG[upgraded ? 5 : 1], textureImg, upgraded, enchanted,
                    null, damage, 0, 0, 0, getFavoriteColor(), 
                    c -> {
                        if (upgraded) {
                            addToBot(new BetterDamageAllEnemiesAction(crtDmgInfo(s, c.damage, damageTypeForTurn), 
                                    AbstractGameAction.AttackEffect.FIRE, false));
                            addToBot(new BetterDamageAllEnemiesAction(crtDmgInfo(s, c.damage, damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.SLASH_HEAVY, false));
                            /*addToBot(new DamageAllEnemiesAction(s, DamageInfo.createDamageMatrix(c.damage, false), 
                                    damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
                            addToBot(new DamageAllEnemiesAction(s, DamageInfo.createDamageMatrix(c.damage, false),
                                    damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));*/
                            return;
                        }
                        addToBot(new VFXAction(new ReaperEffect()));
                        addToBot(new VampireDamageAllEnemiesAction(s, DamageInfo.createDamageMatrix(c.damage, false), 
                                damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
                    });
            NesOptionCard buff = hasAnyPower(s) ? NesOptionCard.createOption(MSG[upgraded ? 6 : 2], MSG[upgraded ? 7 : 3], textureImg, upgraded, enchanted,
                    null, 0, 0, magicNumber, 0, getFavoriteColor(),
                    c -> {
                        int count = c.magicNumber + 1;
                        if (upgraded) {
                            for (int i = 0; i < count; i++)
                                s.powers.stream()
                                        .filter(p -> p.amount > 0)
                                        .forEach(p -> addToBot(new StackPowerAmountAction(s, p, 1)));
                            return;
                        }
                        for (int i = 0; i < count; i++) {
                            s.powers.stream()
                                    .filter(p -> p.amount > 0 && isPowerTypeOf(p, AbstractPower.PowerType.BUFF))
                                    .findAny()
                                    .ifPresent(p -> addToBot(new StackPowerAmountAction(s, p, 1)));
                        }
                    }) : null;
            NesOptionCard blk = upgraded ? NesOptionCard.createOption(MSG[8], MSG[9], textureImg, true, enchanted, 
                    null, 0, block, 0, ExMagicNum, getFavoriteColor(), 
                    c -> {
                        addToBot(new GainBlockAction(s, c.block));
                        rank++;
                        counter = TurnEventListener.currTurn[0];
                        ApplyPowerListener.addNewManipulator(getPrefabid() * "CorruptorDisguise".length() + 1, rank,
                                pm -> TurnEventListener.getPassingTurnsOf(s) - counter < ExMagicNum,
                                (pow, tgt, src) -> {
                                    if (pow != null && tgt == s && (isPowerTypeOf(pow, AbstractPower.PowerType.BUFF)
                                            || isPowerTypeOf(pow, AbstractPower.PowerType.DEBUFF))) {
                                        Log("Negating power: " + pow.name);
                                        addToBot(new CardAboveCreatureAction(s, this));
                                        pow = null;
                                    }
                                    return pow;
                                });
                    }) : null;
            if (canTriggerEnchantedEffect()) {
                dmg.setMultiplier(2);
                if (buff != null) buff.setMultiplier(2);
                if (blk != null) blk.setMultiplier(2);
            }
            addToBot(new ChooseOneAction(cardsToList(dmg, buff, blk)));
        }));
    }
    
    private boolean hasAnyPower(AbstractCreature source) {
        return source != null && !source.isDeadOrEscaped() && !source.powers.isEmpty() 
                && source.powers.stream()
                .anyMatch(p -> p.amount > 0 && (upgraded || isPowerTypeOf(p, AbstractPower.PowerType.BUFF)));
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }

    /*@Override
    public void triggerPostCardDrawn(AbstractCard card) {
        if (canNourishCard(card)) {
            superFlash();
            if (baseDamage > 0) {
                Log("Reinforcing " + getWitherNum() + " damage to card: " + card.name);
                addToBot(new ReinforceCardDamageAction(card, getWitherNum()));
                upgradeDamage(-1);
            }
            if (baseMagicNumber > 0) {
                Log("Reinforcing " + getWitherNum() + " magics to card: " + card.name);
                addToBot(new ReinforceCardMagicAction(card, getWitherNum()));
                upgradeMagicNumber(-1);
            }
            if (upgraded && baseBlock > 0) {
                Log("Reinforcing " + getWitherNum() + " block to card: " + card.name);
                addToBot(new ReinforceCardBlockAction(card, getWitherNum()));
                upgradeBlock(-1);
            }
            reduceWither();
            if (hasCompletelyWithered()) {
                modifyWitheredDescription();
                upgradeBaseCost(0);
            }
        }
    }*/
}