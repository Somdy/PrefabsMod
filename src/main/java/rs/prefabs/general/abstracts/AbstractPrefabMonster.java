package rs.prefabs.general.abstracts;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.cards.DamageSource;
import rs.prefabs.general.misc.PrefabDmgInfo;
import rs.prefabs.general.misc.PrefabClass;
import rs.prefabs.general.monsters.MonsterMoveInfo;
import rs.prefabs.general.utils.Debugger;
import rs.prefabs.general.utils.GeneralUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPrefabMonster extends AbstractMonster implements GeneralUtils {
    private MonsterMoveInfo moveInfo;
    
    protected Map<Integer, MonsterMoveInfo> moves;
    protected PrefabClass<AbstractPrefabMonster> clazz;
    protected boolean movesPreset;

    public AbstractPrefabMonster(String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, float x, float y) {
        super("undetermined", id, maxHealth, hb_x, hb_y, hb_w, hb_h, null, x, y);
        moves = new HashMap<>();
        movesPreset = false;
        this.clazz = new PrefabClass<>(this);
    }
    
    public void setHealth(int max) {
        setHp(max);
    }
    
    public void setHealth(int min, int max) {
        setHp(min, max);
    }

    public PrefabClass<AbstractPrefabMonster> getPrefabClass() {
        return clazz;
    }

    protected void setPrefabType(PrefabClass<AbstractPrefabMonster> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void createIntent() {
        try {
            intent = moveInfo.intent;
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentParticleTimer", 0.5F);
            nextMove = moveInfo.nextMove;
            setIntentBaseDmg(moveInfo.baseDamage);
            if (moveInfo.baseDamage > -1) {
                calculateDamage(getIntentBaseDmg());
                if (moveInfo.isMultiDamage) {
                    ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentMultiAmt", moveInfo.multiplier);
                    ReflectionHacks.setPrivate(this, AbstractMonster.class, "isMultiDmg", true);
                } else {
                    ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentMultiAmt", -1);
                    ReflectionHacks.setPrivate(this, AbstractMonster.class, "isMultiDmg", false);
                }
            }
            Method getIntentImg = ReflectionHacks.getCachedMethod(AbstractMonster.class, "getIntentImg");
            Method getIntentBg = ReflectionHacks.getCachedMethod(AbstractMonster.class, "getIntentBg");
            Method updateIntentTip = ReflectionHacks.getCachedMethod(AbstractMonster.class, "updateIntentTip");
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentImg", getIntentImg.invoke(this));
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentBg", getIntentBg.invoke(this));
            tipIntent = intent;
            intentAlpha = 0F;
            intentAlphaTarget = 1F;
            updateIntentTip.invoke(this);
        } catch (Exception ignr) {
            super.createIntent();
        }
    }
    
    @SpireOverride
    protected void calculateDamage(int dmg) {
        try {
            AbstractCreature target = moveInfo.target;
            float tmp = dmg;
            if (Settings.isEndless && cpr().hasBlight("DeadlyEnemies")) {
                float mod = cpr().getBlight("DeadlyEnemies").effectFloat();
                tmp *= mod;
            }
            for (AbstractPower p : this.powers) {
                tmp = p.atDamageGive(tmp, moveInfo.info.type);
            }
            for (AbstractPower p : target.powers) {
                tmp = p.atDamageReceive(tmp, moveInfo.info.type);
            }
            if (target == cpr()) {
                tmp = cpr().stance.atDamageReceive(tmp, moveInfo.info.type);
            }
            Method applyBackAttack = ReflectionHacks.getCachedMethod(AbstractMonster.class, "applyBackAttack");
            if ((boolean) applyBackAttack.invoke(this)) {
                tmp = MathUtils.ceil(tmp * 1.5F);
            }
            for (AbstractPower p : this.powers) {
                tmp = p.atDamageFinalGive(tmp, moveInfo.info.type);
            }
            for (AbstractPower p : target.powers) {
                tmp = p.atDamageFinalReceive(tmp, moveInfo.info.type);
            }
            dmg = MathUtils.floor(tmp);
            if (dmg < 0) dmg = 0;
            ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", dmg);
        } catch (Exception ignr) {
            SpireSuper.call(dmg);
        }
    }
    
    public AbstractPrefabMonster setMoveInfo(MonsterMoveInfo info) {
        this.moveInfo = info;
        setMove(info);
        return this;
    }

    public MonsterMoveInfo getMoveInfo() {
        return moveInfo;
    }
    
    public AbstractCreature moveTarget() {
        return moveInfo.target;
    }
    
    public MonsterMoveInfo targetMove(int key) {
        return moves.containsKey(key) ? moves.get(key) : new MonsterMoveInfo((byte) -99, Intent.UNKNOWN, this);
    }

    public void setMove(@NotNull MonsterMoveInfo info) {
        this.moveInfo = info;
        setMove(info.moveName, info.nextMove, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void takeTurn() {
        takeMoves();
    }

    @Override
    protected void getMove(int i) {
        getNextMove(i);
    }
    
    protected PrefabDmgInfo crtDmgInfo(@NotNull DamageInfo info) {
        return new PrefabDmgInfo(new DamageSource(info.owner), info.base, info.type);
    }
    
    protected AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }
    
    protected void Log(Object what) {
        Debugger.Log(this, what);
    }

    protected void presetMoves() {}
    
    public abstract void getNextMove(int roll);
    public abstract void takeMoves();
    
    public abstract AbstractPrefabMonster makeCpy(float x, float y);

    public AbstractPrefabMonster makeSameInfoCpy(int minHp, int maxHp, MonsterMoveInfo info, float x, float y) {
        AbstractPrefabMonster m = makeCpy(x, y).setMoveInfo(info);
        m.setHp(minHp, maxHp);
        m.init();
        m.refreshHitboxLocation();
        return m;
    }
    
    public AbstractPrefabMonster makeSameHpCpy(int minHp, int maxHp, float x, float y) {
        AbstractPrefabMonster m = makeCpy(x, y);
        m.setHp(minHp, maxHp);
        m.init();
        m.refreshHitboxLocation();
        return m;
    }
}