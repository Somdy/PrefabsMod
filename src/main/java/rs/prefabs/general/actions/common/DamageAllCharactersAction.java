package rs.prefabs.general.actions.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.misc.PrefabDmgInfo;

public class DamageAllCharactersAction extends AbstractPrefabGameAction {
    private int damage;
    private AttackEffect effect;
    private boolean pureDamage;
    private boolean isFast;

    public DamageAllCharactersAction(int damage, AbstractCreature source, PrefabDmgInfo info, AttackEffect effect,
                                     boolean pureDamage, boolean isFast) {
        this.damage = damage;
        this.source = source;
        this.info = info;
        this.effect = effect;
        this.pureDamage = pureDamage;
        this.isFast = isFast;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            addToBot(new BetterDamageAllEnemiesAction(PrefabDmgInfo.createInfoArray(info, pureDamage), effect, pureDamage, isFast));
            addToBot(new NullableSrcDamageAction(cpr(), info, effect));
        }
        tickDuration();
    }
}