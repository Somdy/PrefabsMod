package rs.prefabs.nemesis.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.abstracts.AbstractPrefabMonster;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.monsters.MonsterMoveInfo;
import rs.prefabs.nemesis.NesFab;

public class Voidremnant extends AbstractNesMonster {
    public static final String ID = NesFab.makeID("Voidremnant");
    public static final int SN = 0;
    private static final String JSON = "PrefabsAssets/NemesisProperties/images/monsters/skeleton.json";
    private static final String ATLAS = "PrefabsAssets/NemesisProperties/images/monsters/skeleton.atlas";
    private final int cH;
    private final int mH;
    private int strAmt;
    private int blkAmt;
    private int debuffAmt;
    private boolean init;
    
    public Voidremnant(int cH, int mH, float x, float y) {
        super(ID, 0, 0, 0, 170F, 190F, x, y);
        this.cH = cH;
        this.mH = mH;
        presetMoves();
        int minHp = MathUtils.ceil(cH > 0 ? cH * 1.65F : mH * 1.85F);
        setHealth(minHp, MathUtils.ceil(minHp * 1.25F));
        loadAnimation(ATLAS, JSON, 1F);
        AnimationState.TrackEntry e = state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        init = true;
    }

    @Override
    public void presetMoves() {
        int baseDmg = 4;
        int debuffDmg = 2;
        strAmt = ascenLevel() < 5 ? 1 : ascenLevel() < 15 ? 2 : 3;
        blkAmt = ascenLevel() < 6 ? 6 : ascenLevel() < 16 ? 8 : 10;
        debuffAmt = ascenLevel() < 4 ? 2 : 3;
        baseDmg += MathUtils.ceil(mH * 0.25F);
        debuffDmg += MathUtils.ceil(mH * 0.15F);
        damage.add(new DamageInfo(this, baseDmg, DamageInfo.DamageType.NORMAL));
        damage.add(new DamageInfo(this, debuffDmg, DamageInfo.DamageType.NORMAL));
        moves.put(1, new MonsterMoveInfo(MOVES[0], (byte) 1, Intent.UNKNOWN, this));
        moves.put(2, new MonsterMoveInfo(MOVES[1], (byte) 2, Intent.UNKNOWN, cpr()));
        moves.put(3, new MonsterMoveInfo(MOVES[2], (byte) 3, Intent.UNKNOWN, this));
        moves.put(4, new MonsterMoveInfo(MOVES[3], (byte) 4, Intent.UNKNOWN, this));
    }

    @Override
    public void getNextMove(int roll) {
        if (init) {
            init = false;
            setMove(targetMove(1));
            return;
        }
        if (lastMove((byte) 1)) {
            setMove(targetMove(2));
        } else if (lastMove((byte) 2)) {
            setMove(targetMove(3));
        } else if (lastMove((byte) 3)) {
            setMove(targetMove(4));
        } else if (lastMove((byte) 4)) {
            setMove(targetMove(1));
        }
    }

    @Override
    public void takeMoves() {
        switch (nextMove) {
            case 1:
                addToBot(new ChangeStateAction(this, "power"));
                addToBot(new ApplyPowerAction(moveTarget(), this, new StrengthPower(moveTarget(), strAmt)));
                addToBot(new GainBlockAction(moveTarget(), this, blkAmt));
                break;
            case 2:
                addToBot(new ChangeStateAction(this, "attack"));
                addToBot(new NullableSrcDamageAction(moveTarget(), crtDmgInfo(damage.get(0)), 
                        AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 3:
                addToBot(new ChangeStateAction(this, "power"));
                addToBot(new HealAction(moveTarget(), this, MathUtils.ceil(moveTarget().currentHealth * 0.25F)));
                addToBot(new GainBlockAction(moveTarget(), this, blkAmt));
                break;
            case 4:
                addToBot(new ChangeStateAction(this, "power"));
                addToBot(new ApplyPowerAction(this, this, new IntangiblePower(this, 1)));
                addToBot(new VFXAction(this, new ShockWaveEffect(hb.cX, hb.cY, new Color(0.5F, 0.2F, 0.2F, 1F), 
                        ShockWaveEffect.ShockWaveType.CHAOTIC), 1.0F));
                addToBot(new QuickAction(AbstractGameAction.ActionType.DAMAGE).setAction(() -> {
                    for (AbstractCreature c : getAllExptCreatures(c -> !c.isDeadOrEscaped() && !(c instanceof Voidremnant))) {
                        addToBot(new NullableSrcDamageAction(c, crtDmgInfo(damage.get(1)), 
                                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        addToBot(new ApplyPowerAction(c, this, new WeakPower(c, debuffAmt, true)));
                    }
                }));
                break;
                
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    public void changeState(String key) {
        switch (key) {
            case "power":
                state.setAnimation(0, "power", false);
                state.addAnimation(0, "idle", true, 0.5F);
                break;
            case "attack":
                state.setAnimation(0, "attack", false);
                state.addAnimation(0, "idle", true, 0.5F);
                break;
        }
    }

    @Override
    public AbstractPrefabMonster makeCpy(float x, float y) {
        return new Voidremnant(cH, mH, x, y);
    }
}