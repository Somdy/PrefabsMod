package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.common.LeechDamageAction;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.cards.PrefabCardTags;
import rs.prefabs.nemesis.actions.common.TeleperceiveAction;
import rs.prefabs.nemesis.patches.NesCardEnum;
import rs.prefabs.nemesis.powers.ParalyticPower;

public class BitterRevelation extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new NullableSrcDamageAction(t, crtDmgInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.BLUNT_HEAVY, false));
        /*addToBot(new LeechDamageAction(t, new DamageInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.BLUNT_HEAVY, false));*/
        addToBot(new ApplyPowerAction(s, s, new ParalyticPower(s, magicNumber)));
        addToBot(new ApplyPowerAction(t, s, new ParalyticPower(t, magicNumber)));
        if (canTriggerEnchantedEffect()) {
            addToBot(new TeleperceiveAction(ExMagicNum, cpr().discardPile, true, c -> c.hasTag(PrefabCardTags.LEECH)));
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