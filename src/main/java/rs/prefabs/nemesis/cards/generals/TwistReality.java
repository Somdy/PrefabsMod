package rs.prefabs.nemesis.cards.generals;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.cards.NesOptionCard;

import java.util.ArrayList;
import java.util.Optional;

public class TwistReality extends AbstractNesGeneralCard {
    private int originBaseCost;
    
    public TwistReality() {
        super();
        addTip(MSG[0], MSG[1]);
        originBaseCost = cost;
    }
    
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (t != null) {
            addToBot(new QuickAction().setAction(() -> {
                final int blk = upgraded ? t.currentBlock : t.currentBlock / 2;
                NesOptionCard block = blk > 0 ? NesOptionCard.createOption(MSG[2], MSG[5] + t.name + MSG[6]
                        + MSG[upgraded ? 7 : 8], textureImg, upgraded, enchanted, getFavoriteColor(), c -> {
                    addToBot(new GainBlockAction(s, t, blk));
                    addToBot(new LoseBlockAction(t, s, blk));
                }) : null;
                Optional<AbstractPower> buff = t.powers.stream()
                        .filter(p -> isPowerTypeOf(p, AbstractPower.PowerType.BUFF) && p instanceof CloneablePowerInterface)
                        .findAny();
                NesOptionCard power = buff.isPresent() && buff.get().amount >= (upgraded ? 2 : 3) ? NesOptionCard.createOption(MSG[3],
                        MSG[5] + t.name + MSG[9] + buff.get().name + MSG[upgraded ? 11 : 10], textureImg, upgraded,
                        enchanted, getFavoriteColor(), c -> {
                            final AbstractPower p = buff.get();
                            final int amount = upgraded ? MathUtils.floor(p.amount / 2F) : MathUtils.floor(p.amount / 3F);
                            if (p instanceof CloneablePowerInterface) {
                                final AbstractPower pc = ((CloneablePowerInterface) p).makeCopy();
                                pc.owner = cpr();
                                pc.amount = amount;
                                pc.updateDescription();
                                addToBot(new ApplyPowerAction(cpr(), cpr(), pc));
                                addToBot(new ReducePowerAction(t, cpr(), p, amount));
                            }
                        }) : null;
                final int healAmt = MathUtils.floor((t.maxHealth - t.currentHealth) * (upgraded ? 0.5F : 0.25F));
                NesOptionCard heal = healAmt > 0 ? NesOptionCard.createOption(MSG[4], MSG[12] + healAmt + MSG[13], textureImg,
                        upgraded, enchanted, getFavoriteColor(), c -> addToBot(new HealAction(cpr(), cpr(), healAmt))) : null;
                final ArrayList<AbstractCard> options = cardsToList(block, power, heal);
                if (!options.isEmpty()) 
                    addToBot(new ChooseOneAction(options));
                else 
                    addToBot(new TalkAction(true, t.name + MSG[14], 1.5F, 1.5F));
            }));
        }
    }

    @Override
    protected void spectralize() {
        if (originBaseCost != cost)
            originBaseCost = cost;
        upgradeBaseCost(1);
    }

    @Override
    protected void despectralize() {
        upgradeBaseCost(originBaseCost);
    }
}