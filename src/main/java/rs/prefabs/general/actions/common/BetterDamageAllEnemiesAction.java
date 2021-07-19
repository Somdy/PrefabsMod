package rs.prefabs.general.actions.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.misc.PrefabDmgInfo;

public class BetterDamageAllEnemiesAction extends AbstractPrefabGameAction {
    public PrefabDmgInfo[] infos;
    private PrefabDmgInfo baseInfo;
    private boolean firstFrame;
    private boolean initializedInfos;
    private boolean pureDmg;
    
    public BetterDamageAllEnemiesAction(PrefabDmgInfo[] infos, AttackEffect effect, boolean pureDmg, boolean isFast) {
        this.infos = infos;
        this.baseInfo = infos[0];
        this.firstFrame = true;
        this.initializedInfos = false;
        this.attackEffect = effect;
        this.pureDmg = pureDmg;
        duration = isFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST;
    }

    public BetterDamageAllEnemiesAction(PrefabDmgInfo info, AttackEffect effect, boolean isFast) {
        this.infos = null;
        this.baseInfo = info;
        this.firstFrame = true;
        this.initializedInfos = true;
        this.attackEffect = effect;
        this.pureDmg = false;
        duration = isFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (firstFrame) {
            int count = AbstractDungeon.getMonsters().monsters.size();
            if (initializedInfos || infos == null) {
                infos = PrefabDmgInfo.createInfoArray(baseInfo, pureDmg);
            }
            boolean musicPlayed = false;
            for (int i = 0; i < count; i++) {
                AbstractMonster m  = AbstractDungeon.getMonsters().monsters.get(i);
                if (m != null && !m.isDeadOrEscaped()) {
                    if (musicPlayed)
                        effectToList(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, attackEffect, true));
                    else {
                        musicPlayed = true;
                        effectToList(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, attackEffect));
                    }
                }
            }
            firstFrame = false;
        }
        tickDuration();
        if (isDone) {
            int[] damage = new int[infos.length];
            for (int i = 0; i < infos.length; i++) {
                damage[i] = infos[i].output;
            }
            for (AbstractPower p : cpr().powers) {
                p.onDamageAllEnemies(damage);
            }
            int count = AbstractDungeon.getMonsters().monsters.size();
            for (int i = 0; i < count; i++) {
                AbstractMonster m  = AbstractDungeon.getMonsters().monsters.get(i);
                if (m != null && !m.isDeadOrEscaped()) {
                    if (attackEffect == AttackEffect.POISON) {
                        m.tint.color.set(Color.CHARTREUSE.cpy());
                        m.tint.changeColor(Color.WHITE.cpy());
                    } else if (attackEffect == AttackEffect.FIRE) {
                        m.tint.color.set(Color.RED.cpy());
                        m.tint.changeColor(Color.WHITE.cpy());
                    }
                    m.damage(infos[i]);
                    if (infos[i].source.isLeech() && m.lastDamageTaken > 0) {
                        int healAmt = MathUtils.floor(m.lastDamageTaken / 2F);
                        addToTop(new HealAction(infos[i].source.getSource(), infos[i].source.getSource(), healAmt));
                    }
                }
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            if (!Settings.FAST_MODE) {
                addToTop(new WaitAction(0.1F));
            }
        }
    }
}