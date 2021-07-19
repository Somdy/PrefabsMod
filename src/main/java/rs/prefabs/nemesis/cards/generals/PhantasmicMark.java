package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.interfaces.IndicatorInvoker;
import rs.prefabs.nemesis.cards.NesOptionCard;
import rs.prefabs.nemesis.powers.PhantasmicMarkPower;

public class PhantasmicMark extends AbstractNesGeneralCard implements IndicatorInvoker {
    
    public PhantasmicMark() {
        super();
        addTip(MSG[0], MSG[1]);
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (!isInEnemyUse()) {
            addToBot(new QuickAction().setAction(() -> {
                NesOptionCard defend = NesOptionCard.createOption(name + ":" + MSG[2], MSG[3] + block + MSG[4], textureImg, 
                        upgraded, enchanted, getFavoriteColor(), c -> addToBot(
                                new ApplyPowerAction(s, s, new PhantasmicMarkPower(s, s, 1, block, true))));
                NesOptionCard hunt = NesOptionCard.createOption(name + ":" + MSG[5], MSG[6] + magicNumber + MSG[7], textureImg, 
                        upgraded, enchanted, getFavoriteColor(),
                        c -> active(new Vector2(c.current_x, c.current_y), crt -> addToBot(
                                new ApplyPowerAction(crt, s, new PhantasmicMarkPower(crt, s, magicNumber, 0, false))), 
                                crt -> !(crt instanceof AbstractPlayer)));
                addToBot(new ChooseOneAction(cardsToList(defend, hunt)));
                if (canTriggerEnchantedEffect()) {
                    addToBot(new DrawCardAction(s, ExMagicNum, false));
                }
            }));
        }
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[1]);
    }

    @Override
    public boolean canEnemyUse() {
        return true;
    }
}