package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.actions.common.TeleperceiveAction;
import rs.prefabs.nemesis.cards.NesOptionCard;
import rs.prefabs.nemesis.powers.DamageNextTurnPower;
import rs.prefabs.nemesis.powers.LoseHPNextTurnPower;

public class EtherealPain extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        final int dmg = upgraded ? 10 : MathUtils.floor(s.currentHealth * 0.2F);
        addToBot(new ApplyPowerAction(s, s, (canTriggerEnchantedEffect() ? 
                new DamageNextTurnPower(s, crtDmgInfo(s, dmg, DamageInfo.DamageType.THORNS), name)
                : new LoseHPNextTurnPower(s, dmg, name))));
        addToBot(new QuickAction().setAction(() -> {
            NesOptionCard draw = NesOptionCard.createOption(name + ":" + MSG[0], MSG[2] + magicNumber + MSG[3], textureImg, 
                    upgraded, enchanted, getFavoriteColor(), c -> addToBot(new DrawCardAction(magicNumber)));
            NesOptionCard tele = NesOptionCard.createOption(name + ":" + MSG[1], MSG[4] + ExMagicNum + MSG[5], textureImg, 
                    upgraded, enchanted, getFavoriteColor(), c -> addToBot(new TeleperceiveAction(ExMagicNum, cpr().drawPile, false)
                            .setAnyNumber(false)
                            .setCanCancel(false)));
            NesOptionCard defend = NesOptionCard.createOption(name + ":" + MSG[6], MSG[7] + block + MSG[8], textureImg, 
                    upgraded, enchanted, getFavoriteColor(), c -> addToBot(new GainBlockAction(cpr(), cpr(), block)));
            addToBot(new ChooseOneAction(cardsToList(draw, tele, defend)));
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
}