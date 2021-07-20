package rs.prefabs.general.abstracts;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.prefabs.general.cards.DamageSource;
import rs.prefabs.general.misc.PrefabDmgInfo;
import rs.prefabs.general.utils.Debugger;
import rs.prefabs.general.utils.GeneralUtils;

public abstract class AbstractPrefabPower extends AbstractPower implements CloneablePowerInterface, GeneralUtils {
    public static TextureAtlas powerAtlas;
    public int extraAmt = -1;
    public boolean canExtraAmtGoNegative;
    public boolean isExtraAmtFixed;
    public boolean stackable;
    protected Color red = new Color(1.0F, 0.0F, 0.0F, 1.0F);
    protected Color blue = new Color(0.0F, 0.0F, 1.0F, 1.0F);
    protected float extraAmtFontScale;
    public AbstractCreature source;

    public AbstractPrefabPower() {
        powerAtlas = getPowerAtlas();
        type = AbstractPower.PowerType.BUFF;
        isTurnBased = false;
        isPostActionPower = false;
        canGoNegative = false;
        canExtraAmtGoNegative = false;
        isExtraAmtFixed = true;
        stackable = true;
        extraAmtFontScale = fontScale;
    }
    
    public static class ExtraPowerType {
        @SpireEnum(name = "Special")
        public static PowerType SPECIAL;
    }

    protected void setDefaults(String ID, String name, PowerType type) {
        this.ID = ID;
        this.name = name;
        this.type = type;
    }

    protected void setValues(AbstractCreature owner, AbstractCreature source, int amount) {
        this.owner = owner;
        this.source = source;
        this.amount = amount;
        stackable = amount <= -1;
    }

    protected void setValues(AbstractCreature owner, AbstractCreature source, int amount, int extraAmt) {
        this.owner = owner;
        this.source = source;
        this.amount = amount;
        this.extraAmt = extraAmt;
        stackable = amount <= -1;
    }

    protected void setValues(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        stackable = amount <= -1;
    }

    protected void setValues(AbstractCreature owner, int amount, int extraAmt) {
        this.owner = owner;
        this.amount = amount;
        this.extraAmt = extraAmt;
        stackable = amount <= -1;
    }

    protected void loadImg(String name) {
        if (powerAtlas == null)
            powerAtlas = getPowerAtlas();
        this.region128 = powerAtlas.findRegion("128/" + name);
        this.region48 = powerAtlas.findRegion("48/" + name);
    }

    public int getMaxAmount() {
        return 999;
    }

    public int getMaxExtraAmount() {
        return 999;
    }

    protected AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackable || amount > 0) {
            fontScale = 8F;
            amount += stackAmount;
            if (amount > getMaxAmount()) amount = getMaxAmount();
        } else {
            Log(" does not stack");
        }
    }
    
    public void stackExtraAmount(int stackAmount) {
        if (!isExtraAmtFixed || extraAmt <= 0) {
            extraAmtFontScale = 0F;
            extraAmt += stackAmount;
            if (extraAmt > getMaxExtraAmount()) extraAmt = getMaxExtraAmount();
        } else {
            Log(" does not stack second value");
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        if (this.extraAmt > 0) {
            if (isExtraAmtFixed) {
                blue.a = c.a;
                c = blue;
            }
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.extraAmt),
                    x, y + 16.0F * Settings.scale, extraAmtFontScale, c);
        }
        else if (this.extraAmt < 0 && canExtraAmtGoNegative) {
            red.a = c.a;
            c = red;
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.extraAmt),
                    x, y + 16.0F * Settings.scale, extraAmtFontScale, c);
        }
    }

    protected PrefabDmgInfo crtDmgInfo(AbstractCreature source, int base, DamageInfo.DamageType type) {
        return new PrefabDmgInfo(crtDmgSrc(source), base, type);
    }

    protected DamageSource crtDmgSrc(AbstractCreature source) {
        return new DamageSource(source);
    }
    
    protected void Log(Object what) {
        Debugger.Log(this, what);
    }
    
    protected abstract TextureAtlas getPowerAtlas();
    
    public float modifyOnGainingBlock(float blockAmount) {
        return blockAmount;
    }
}