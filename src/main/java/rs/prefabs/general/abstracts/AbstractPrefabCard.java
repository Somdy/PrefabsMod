package rs.prefabs.general.abstracts;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rs.prefabs.general.annotations.Replaced;
import rs.prefabs.general.cards.DamageSource;
import rs.prefabs.general.misc.PrefabDmgInfo;
import rs.prefabs.general.data.CardData;
import rs.prefabs.general.interfaces.TemplateItem;
import rs.prefabs.general.misc.PrefabClass;
import rs.prefabs.general.utils.GeneralUtils;

import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractPrefabCard extends CustomCard implements GeneralUtils {
    private static Map<String, Texture> images = new HashMap<>();
    protected List<TooltipInfo> tips;
    
    private final int prefabid;
    private CardData.SData data;
    private Predicate<AbstractPrefabCard> inEnemyUse;

    protected PrefabClass<AbstractPrefabCard> clazz;
    protected AbstractCreature enemyUser;
    
    public AbstractPrefabCard(@NotNull CardData data, CardColor color) {
        super("undetermined", "undetermined", "PrefabsAssets/GeneralProperties/images/card/wildcard.png", -2, "undetermined", CardType.SKILL, color, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.prefabid = returnPrefabUniqueID();
        this.data = data.getSData(prefabid);
        this.clazz = new PrefabClass<>(this);
        inEnemyUse = card -> false;
        enemyUser = null;
        assignBasics();
        this.textureImg = getImageUrl();
        loadCardImage(textureImg);
        setDamageType(DamageInfo.DamageType.NORMAL);
    }
    
    protected void assignBasics() {
        if (!canAssignBasics()) return;
        if (data == null)
            throw new NullPointerException("Unable to find target card data whose id = " + prefabid);
        setCardID(data.getId());
        setCostValue(data.getBaseCost(), true);
        misc = data.getMisc();
        setDamageValue(data.getBaseDmg(), true);
        setBlockValue(data.getBaseBlock(), true);
        setMagicValue(data.getBaseMagics(), true);
        this.type = data.getType();
        this.rarity = data.getRarity();
        this.target = data.getTarget();
        List<CardTags> tags = data.getTags();
        if (tags != null) addTags(tags);
        CardData.CardAttributes attrs = data.getAttributes();
        this.exhaust = attrs.isExhaust();
        this.selfRetain = attrs.isSelfRetain();
        this.retain = attrs.isRetain();
        this.isInnate = attrs.isInnate();
        this.isEthereal = attrs.isEthereal();
        this.purgeOnUse = attrs.isPurge();
    }

    public int getPrefabid() {
        return prefabid;
    }

    public PrefabClass<AbstractPrefabCard> getPrefabClass() {
        return clazz;
    }

    protected void setPrefabType(PrefabClass<AbstractPrefabCard> clazz) {
        this.clazz = clazz;
    }
    
    protected CardData.SData data() {
        return data;
    }

    public boolean isInEnemyUse() {
        return inEnemyUse.test(this);
    }

    public boolean isInEnemyUse(AbstractPrefabCard card) {
        return inEnemyUse.test(card);
    }
    
    public final void setEnemyUsing(@NotNull AbstractCreature enemyUser) {
        this.enemyUser = enemyUser;
        inEnemyUse = c -> this.enemyUser != null && !this.enemyUser.isDeadOrEscaped();
    }
    
    protected boolean canAssignBasics() {
        return !(this instanceof TemplateItem) && !this.getClass().isAnnotationPresent(Replaced.class);
    }
    
    @Nullable
    protected AbstractCreature findAnyTarget(boolean userIncluded) {
        List<AbstractCreature> candicates = new ArrayList<>();
        candicates.add(cpr());
        AbstractDungeon.getMonsters().monsters.forEach(m -> {
            if (!m.isDeadOrEscaped() && !m.halfDead && !candicates.contains(m))
                candicates.add(m);
        });
        if (!userIncluded) {
            if (isInEnemyUse())
                candicates.removeIf(c -> !(c instanceof AbstractPlayer));
            else 
                candicates.remove(cpr());
        }
        return candicates.isEmpty() ? null : getRandom(candicates, cardRandomRng()).orElse(null);
    }

    protected void addTip(String head, String body) {
        if (tips == null)
            tips = new ArrayList<>();
        if (tips.stream().anyMatch(t -> t.title.equals(head) && t.description.equals(body)))
            return;
        tips.add(new TooltipInfo(head, body));
    }
    
    protected boolean replaceTip(String head, String body) {
        if (tips == null)
            return false;
        for (TooltipInfo tip : tips) {
            if (tip.title.equals(head) && !tip.description.equals(body)) {
                tip.description = body;
                return true;
            }
        }
        return false;
    }

    protected boolean removeTip(String head) {
        return removeTip(head, null);
    }

    protected boolean removeTip(String head, String body) {
        if (tips == null)
            return false;
        return tips.removeIf(t -> body == null ? t.title.equals(head) : (t.title.equals(head) && t.description.equals(body)));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return tips;
    }

    @Override
    public void update() {
        onUpdating();
        super.update();
    }

    protected boolean inHand() {
        return inHand(this);
    }
    
    protected boolean inHand(AbstractCard card) {
        return cpr().hand.contains(card);
    }
    
    protected boolean addTags(CardTags... tags) {
        return this.tags.addAll(Arrays.asList(tags));
    }
    
    protected boolean addTags(List<CardTags> tags) {
        return this.tags.addAll(tags);
    }

    @Override
    protected void addToBot(AbstractGameAction action) {
        if (action != null) 
            super.addToBot(action);
    }

    @Override
    protected void addToTop(AbstractGameAction action) {
        if (action != null)
            super.addToTop(action);
    }

    protected PrefabDmgInfo crtDmgInfo(AbstractCreature source, int base, DamageInfo.DamageType type) {
        return new PrefabDmgInfo(crtDmgSrc(source), base, type);
    }
    
    protected DamageSource crtDmgSrc(AbstractCreature source) {
        return new DamageSource(source, this);
    }
    
    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public void setCostValue(int num, boolean ignoreModifier) {
        if (!ignoreModifier) {
            if (isCostModifiedForTurn || isCostModified || upgradedCost) return;
        }
        cost = costForTurn = num;
    }

    public void setDamageValue(int num, boolean ignoreModifier) {
        if (!ignoreModifier) {
            if (isDamageModified || upgradedDamage) return;
        }
        damage = baseDamage = num;
        if (baseDamage < 0) baseDamage = 0;
    }

    public void setBlockValue(int num, boolean ignoreModifier) {
        if (!ignoreModifier) {
            if (isBlockModified || upgradedBlock) return;
        }
        block = baseBlock = num;
        if (baseBlock < 0) baseBlock = 0;
    }

    public void setMagicValue(int num, boolean ignoreModifier) {
        if (!ignoreModifier) {
            if (isMagicNumberModified || upgradedMagicNumber) return;
        }
        magicNumber = baseMagicNumber = num;
        if (baseMagicNumber < 0) baseMagicNumber = 0;
    }
    
    public void setDamageType(DamageInfo.DamageType type) {
        damageType = damageTypeForTurn = type;
    }

    @NotNull
    public TextureAtlas.AtlasRegion getRegionFromImages(String path) {
        Texture t = getTextureFromString(path);
        return new TextureAtlas.AtlasRegion(t, 0, 0, t.getWidth(), t.getHeight());
    }

    private static Texture getTextureFromString(String path) {
        loadTextureFromString(path);
        return images.get(path);
    }

    private static void loadTextureFromString(String path) {
        if (!images.containsKey(path))
            images.put(path, ImageMaster.loadImage(path));
    }

    protected AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }

    @Override
    public void upgrade() {
        promote();
    }
    
    protected void upgradeNewCost() {
        boolean diff = cost != data.getPrmtNewCost();
        if (diff) upgradeBaseCost(data.getPrmtNewCost());
    }
    
    protected void upgradeDamage() {
        boolean diff = baseDamage != data.getPrmtDmg() + baseDamage;
        if (diff) upgradeDamage(data.getPrmtDmg());
    }
    
    protected void upgradeBlock() {
        boolean diff = baseBlock != data.getPrmtBlock() + baseBlock;
        if (diff) upgradeBlock(data.getPrmtBlock());
    }
    
    protected void upgradeMagics() {
        boolean diff = baseMagicNumber != data.getPrmtMagics() + baseMagicNumber;
        if (diff) upgradeMagicNumber(data.getPrmtMagics());
    }
    
    protected void upgradeAttributes() {
        CardData.CardAttributes attrs = data.getAttributes();
        if (attrs.isExhautedChanged()) 
            this.exhaust = attrs.isPrmtExhaust();
        if (attrs.isSelfRetainChanged())
            this.selfRetain = attrs.isPrmtSelfRetain();
        if (attrs.isRetainChanged())
            this.retain = attrs.isPrmtRetain();
        if (attrs.isInnateChanged())
            this.isInnate = attrs.isPrmtInnate();
        if (attrs.isEtherealChanged())
            this.isEthereal = attrs.isPrmtEthereal();
        if (attrs.isPurgeChanged())
            this.purgeOnUse = attrs.isPrmtPurge();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        onPlay(p, m);
    }
    
    public abstract int returnPrefabUniqueID();
    public abstract void onPlay(AbstractCreature s, AbstractCreature t);
    public abstract void promote();
    @NotNull
    protected abstract String getImageUrl();
    public abstract boolean canEnemyUse();
    
    protected void onUpdating() {}
    public void triggerPostCardDrawn(AbstractCard card) {}
}