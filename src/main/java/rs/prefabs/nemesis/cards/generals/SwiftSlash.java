package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.annotations.Replaced;
import rs.prefabs.nemesis.actions.common.TeleperceiveAction;

@Deprecated
@Replaced(substitute = Overflow.class)
public class SwiftSlash extends AbstractNesGeneralCard {
    
    public SwiftSlash() {
        super();
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction(AbstractGameAction.ActionType.DAMAGE)
                .setAction(() -> {
                    for (int i = 0; i < magicNumber; i++) {
                        addToBot(new NullableSrcDamageAction(AbstractDungeon.getRandomMonster(), 
                                crtDmgInfo(s, damage, damageTypeForTurn), 
                                getRandom(objsToList(AbstractGameAction.AttackEffect.SLASH_DIAGONAL, 
                                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                                        AbstractGameAction.AttackEffect.SLASH_VERTICAL)).get()));
                    }
                    addToBot(new GainEnergyAction(1));
                    if (canTriggerEnchantedEffect())
                        addToBot(new TeleperceiveAction(ExMagicNum, cpr().drawPile, true, 
                                c -> isCardTypeOf(c, CardType.ATTACK)));
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