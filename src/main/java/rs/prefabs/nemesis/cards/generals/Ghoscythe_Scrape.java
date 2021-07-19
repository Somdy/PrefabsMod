package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.annotations.Replaced;
import rs.prefabs.nemesis.actions.common.TeleperceiveAction;

@Deprecated
@Replaced(substitute = LostPuppet.class)
public class Ghoscythe_Scrape extends AbstractNesGeneralCard {
    
    public Ghoscythe_Scrape() {
        super();
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction().setAction(() -> {
            int totalDmg = damage;
            if (totalDmg <= 50) {
                int count = 0;
                while (count < totalDmg) {
                    AbstractMonster m = AbstractDungeon.getRandomMonster();
                    if (m == null || m.isDeadOrEscaped()) continue;
                    addToBot(new NullableSrcDamageAction(m, crtDmgInfo(s, 1, damageTypeForTurn),
                            getRandom(objsToList(AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                                    AbstractGameAction.AttackEffect.SLASH_VERTICAL)).get()));
                    count++;
                }
            } else {
                int enemies = (int) AbstractDungeon.getMonsters().monsters.stream()
                        .filter(m -> m != null && !m.isDeadOrEscaped()).mapToInt(m -> 1).count();
                int times = totalDmg / enemies;
                int res = totalDmg - times * enemies;
                int count = 0;
                while (count < enemies) {
                    int num = 0;
                    while (num < times) {
                        AbstractMonster m = AbstractDungeon.getRandomMonster();
                        if (m == null || m.isDeadOrEscaped()) continue;
                        addToBot(new NullableSrcDamageAction(m, crtDmgInfo(s, 1, damageTypeForTurn),
                                getRandom(objsToList(AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                                        AbstractGameAction.AttackEffect.SLASH_VERTICAL)).get()));
                        num++;
                    }
                    count++;
                }
                if (res > 0) {
                    addToBot(new DamageRandomEnemyAction(new DamageInfo(s, res, damageTypeForTurn), 
                            AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
            }
        }).setFollowupAction(new TeleperceiveAction(magicNumber, cpr().discardPile, !canTriggerEnchantedEffect(), 
                c -> isCardTypeOf(c, CardType.ATTACK) && c != this)));
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