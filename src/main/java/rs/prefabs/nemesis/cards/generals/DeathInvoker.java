package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import rs.prefabs.general.actions.common.DamageAndDoWhenFatal;
import rs.prefabs.general.actions.utility.QuickAction;

public class DeathInvoker extends AbstractNesGeneralCard {
    private boolean copy;
    
    public DeathInvoker() {
        super();
        baseDamage = misc;
        addTip(MSG[0], MSG[1] + MathUtils.floor(getCardRealCost(this) / 2F)
                + MSG[2] + MathUtils.floor(damage / 2F) + MSG[3]);
        copy = false;
    }

    public DeathInvoker(boolean copy) {
        this();
        this.copy = copy;
        removeTip(MSG[0]);
        upgradeDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new VFXAction(new ClashEffect(t.hb.cX, t.hb.cY), 0.1F));
        addToBot(new DamageAndDoWhenFatal(t, crtDmgInfo(s, damage, damageTypeForTurn), crt -> {
            for (AbstractCard card : cpr().masterDeck.group) {
                if (card.uuid.equals(DeathInvoker.this.uuid)) {
                    card.misc += magicNumber;
                    card.applyPowers();
                    card.baseDamage = card.misc;
                    card.isDamageModified = false;
                }
            }
            for (AbstractCard card : GetAllInBattleInstances.get(DeathInvoker.this.uuid)) {
                card.misc += magicNumber;
                card.baseDamage = card.misc;
                card.applyPowers();
            }
        }).setFollowupAction(new QuickAction().setAction(this::spilt)));
    }

    @Override
    protected void spectralize() {
        enchantMagics(0, baseMagicNumber + 2);
    }

    @Override
    protected void despectralize() {
        disenchantMagics(0);
    }

    @Override
    protected void onUpdating() {
        replaceTip(MSG[0], MSG[1] + MathUtils.floor(getCardRealCost(this) / 2F)
                + MSG[2] + MathUtils.floor(damage / 2F) + MSG[3]);
    }

    @Override
    public AbstractCard makeCopy() {
        if (copy)
            return new DeathInvoker(true);
        return super.makeCopy();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        if (copy) {
            DeathInvoker invoker = (DeathInvoker) super.makeStatEquivalentCopy();
            invoker.uuid = this.uuid;
            return invoker;
        }
        return super.makeStatEquivalentCopy();
    }

    private void spilt() {
        if (copy) return;
        DeathInvoker invoker = new DeathInvoker(true);
        invoker.uuid = this.uuid;
        invoker.setCostValue(MathUtils.floor(getCardRealCost(this) / 2F), true);
        invoker.setDamageValue(MathUtils.floor(this.damage / 2F), true);
        addToBot(new MakeTempCardInDrawPileAction(invoker, 2, true, true));
    }
}