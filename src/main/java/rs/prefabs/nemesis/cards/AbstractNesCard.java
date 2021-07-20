package rs.prefabs.nemesis.cards;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.abstracts.AbstractPrefabCard;
import rs.prefabs.general.actions.common.ReinforceCardBlockAction;
import rs.prefabs.general.actions.common.ReinforceCardDamageAction;
import rs.prefabs.general.actions.common.ReinforceCardMagicAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.misc.SubPrefabClass;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.data.CardData;
import rs.prefabs.general.localizations.PrefabCardStrings;
import rs.prefabs.general.utils.PatternConstructor;
import rs.prefabs.general.utils.PatternMgr;
import rs.prefabs.nemesis.NesDebug;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.actions.common.ReinforceCardExMagicAction;
import rs.prefabs.nemesis.character.OfferHelper;
import rs.prefabs.nemesis.dynamics.NesExMagicNum;
import rs.prefabs.nemesis.dynamics.NesWitherNum;
import rs.prefabs.nemesis.interfaces.AbstractOffering;
import rs.prefabs.nemesis.patches.NesCardEnum;
import rs.prefabs.nemesis.powers.EidolonFormPower;
import rs.prefabs.nemesis.utils.NesSK;
import rs.prefabs.nemesis.utils.NesGeneralUtils;
import rs.prefabs.nemesis.utils.NesPatterMgr;
import rs.prefabs.nemesis.utils.SwitchMgr;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public abstract class AbstractNesCard extends AbstractPrefabCard implements NesGeneralUtils, CustomSavable<Map<String, String>> {
    protected static final PrefabCardStrings NEMESIS_CARD_STRINGS = NesFab.getNemesispack().getCardStrings(NesFab.makeID("NemesisCard"));
    protected static final String WITHERABLE_TEXT = NEMESIS_CARD_STRINGS.MSG[0];
    protected static final String WITHERED_TEXT = NEMESIS_CARD_STRINGS.MSG[1];
    protected static final String CONTENTABLE_HEAD = NEMESIS_CARD_STRINGS.MSG[2];
    protected static final String CONTENTABLE_BODY = NEMESIS_CARD_STRINGS.MSG[3];
    protected static final String CONTENTED_HEAD = NEMESIS_CARD_STRINGS.MSG[4];
    protected static final String CONTENTED_BODY = NEMESIS_CARD_STRINGS.MSG[5];
    public static final String OFFERING_TYPE = NEMESIS_CARD_STRINGS.MSG[6];
    
    protected final PrefabCardStrings cardStrings;
    protected final String FAKENAME;
    protected final String TRUENAME;
    protected final String DESCRIPTION;
    protected final String UPGRADE_DESCRIPTION;
    protected final String[] EXTENDED_DESCRIPTION;
    protected final String[] MSG;
    
    protected Predicate<AbstractCard> canCardOffer;
    protected Predicate<AbstractCard> canTriggerEnchantment;
    protected boolean enchanted;
    protected String enchantDesc;
    protected String originDesc;
    protected int ExMagicNum;
    protected int baseExMagicNum;
    protected boolean isExMagicNumModified;
    protected boolean upgradedExMagicNum;
    protected int baseWitherNum;
    protected int WitherNum;
    protected boolean isWitherNumModified;
    protected boolean upgradedWitherNum;
    
    private Integer[] oVs;
    private Integer[] tVs;
    private Integer[] increments;
    
    protected static final String TEMPLATE_KEY = "TemplateCard";
    public static final Color ENCHANTED_COLOR = Color.PURPLE.cpy();
    
    public AbstractNesCard(CardData data) {
        super(data, NesCardEnum.Nemesis);
        
        cardStrings = NesFab.getNemesispack().getCardStrings(cardID);
        FAKENAME = cardStrings.FAKENAME;
        TRUENAME = cardStrings.TRUENAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
        MSG = cardStrings.MSG;
        
        canCardOffer = this::isCardOfferable;
        canTriggerEnchantment = c -> true;
        enchanted = false;
        enchantDesc = null;
        originDesc = null;
        ExMagicNum = baseExMagicNum = -1;
        upgradedExMagicNum = isMagicNumberModified = false;
        WitherNum = baseWitherNum = -1;
        upgradedWitherNum = isWitherNumModified = false;
        
        oVs = new Integer[] {-99, -99, -99, -99};
        tVs = new Integer[] {-99, -99, -99, -99};
        increments = new Integer[] {-99, -99, -99, -99};

        setPrefabType(clazz.create(new SubPrefabClass<AbstractNesCard>().create(getKey())));
        setCustomValues();
        setInitialCardName(FAKENAME);
        setInitialRawDesc(DESCRIPTION);
        initTags();
        
        onInit();
    }
    
    protected void onInit() {}

    @Override
    public Map<String, String> onSave() {
        Map<String, String> map = createCustomMapOnSaving();
        map.put("enchanted", String.valueOf(enchanted));
        return map;
    }
    
    @Override
    public Type savedType() {
        return new TypeToken<Map<String, String>>(){}.getType();
    }
    
    protected Map<String, String> createCustomMapOnSaving() {
        return new HashMap<>();
    }

    @Override
    public void onLoad(Map<String, String> map) {
        receiveCustomMapOnLoading(map);
        boolean hasEnchanted = false;
        if (map.containsKey("enchanted")) {
            hasEnchanted = Boolean.parseBoolean(map.get("enchanted"));
        }
        if (hasEnchanted) restoreEnchantmentOnSL();
    }
    
    protected void receiveCustomMapOnLoading(Map<String, String> map) {}

    private void setCustomValues() {
        if (!canAssignBasics()) return;
        if (data().getCustom("baseexmagicnum") != null) {
            int exmagic = Integer.parseInt(data().getCustom("baseexmagicnum"));
            ExMagicNum = baseExMagicNum = exmagic;
        }
        if (data().getCustom("basewithernum") != null) {
            int wither = Integer.parseInt(data().getCustom("basewithernum"));
            WitherNum = baseWitherNum = wither;
        }
    }

    @SpireOverride
    protected void renderPortraitFrame(SpriteBatch sb, float x, float y) { 
        if (isEnchanted()) {
            renderEnchantedFrame(sb, x, y);
            return;
        }
        SpireSuper.call(sb, x, y);
    }

    private void renderEnchantedFrame(SpriteBatch sb, float x, float y) {
        switch (type) {
            case ATTACK:
                renderHelper(sb, ENCHANTED_COLOR, getRegionFromImages("PrefabsAssets/NemesisProperties/images/cardui/512/frame_attack.png"), x, y);
                break;
            case SKILL:
                renderHelper(sb, ENCHANTED_COLOR, getRegionFromImages("PrefabsAssets/NemesisProperties/images/cardui/512/frame_skill.png"), x, y);
                break;
            case POWER:
                renderHelper(sb, ENCHANTED_COLOR, getRegionFromImages("PrefabsAssets/NemesisProperties/images/cardui/512/frame_power.png"), x, y);
                break;
            default:
        }
    }

    @SpireOverride
    protected void renderBannerImage(SpriteBatch sb, float drawX, float drawY) {
        if (isEnchanted()) {
            renderEnchantedBanner(sb, drawX, drawY);
            return;
        }
        SpireSuper.call(sb, drawX, drawY);
    }

    private void renderEnchantedBanner(SpriteBatch sb, float drawX, float drawY) {
        renderHelper(sb, ENCHANTED_COLOR, getRegionFromImages("PrefabsAssets/NemesisProperties/images/cardui/512/banner.png"), drawX, drawY);
    }

    private void renderHelper(@NotNull SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY) {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - img.originalWidth / 2F, drawY + img.offsetY - img.originalHeight / 2F,
                img.originalWidth / 2F - img.offsetX, img.originalHeight / 2F - img.offsetY,
                img.packedWidth, img.packedHeight, scale(drawScale), scale(drawScale), angle);
    }
    
    @SpireOverride
    protected void renderType(SpriteBatch sb) {
        if (hasTag(NesCardEnum.OFFERING)) {
            BitmapFont font = FontHelper.cardTypeFont;
            font.getData().setScale(drawScale);
            Color typeColor;
            try {
                typeColor = getTypeColor();
            } catch (Exception e) {
                typeColor = Color.WHITE.cpy();
            }
            FontHelper.renderRotatedText(sb, font, OFFERING_TYPE, current_x, current_y - scale(22F * drawScale), 
                    0F, -scale(drawScale), angle, false, typeColor);
            return;
        }
        SpireSuper.call(sb);
    }
    
    @NotNull
    private Color getTypeColor() throws Exception {
        Field typeColor = AbstractCard.class.getDeclaredField("typeColor");
        typeColor.setAccessible(true);
        Color color = (Color) typeColor.get(this);
        Field renderColor = AbstractCard.class.getDeclaredField("renderColor");
        renderColor.setAccessible(true);
        Color rC = (Color) renderColor.get(this);
        color.a = rC.a;
        return color;
    }

    @SpireOverride
    protected void renderTitle(SpriteBatch sb) {
        BitmapFont font = FontHelper.cardTitleFont;
        boolean useSmallTitle = false;
        try {
            useSmallTitle = getCardTitleFont();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (useSmallTitle) {
            font.getData().setScale(drawScale * 0.85F);
        } else {
            font.getData().setScale(drawScale);
        }
        if (upgraded || enchanted) {
            Color color;
            if (upgraded && !enchanted)
                color = getFavoriteColor();
            else if (!upgraded)
                color = complement(ENCHANTED_COLOR);
            else {
                color = Color.GOLDENROD.cpy();
            }
            color.a = transparency;
            FontHelper.renderRotatedText(sb, font, name, current_x, current_y, 0F, scale(175F * drawScale),
                    angle, false, color);
            return;
        }
        SpireSuper.call(sb);
    }

    @Override
    public void initializeDescription() {
        preModifyDescription();
        super.initializeDescription();
    }
    
    private void preModifyDescription() {
        rawDescription = checkWithPatterns(rawDescription);
    }

    private boolean getCardTitleFont() throws Exception {
        Field useSmallTitleFont = AbstractCard.class.getDeclaredField("useSmallTitleFont");
        useSmallTitleFont.setAccessible(true);
        return useSmallTitleFont.getBoolean(this);
    }

    @Override
    public void renderInLibrary(SpriteBatch sb) {
        boolean viewingEnchanted = SwitchMgr.isViewingEnchanted;
        boolean viewingUpgrade = SingleCardViewPopup.isViewingUpgrade;
        if (isOnScreen() && isSeen && !isLocked  && (viewingEnchanted || viewingUpgrade)) {
            if (viewingUpgrade && !viewingEnchanted) {
                super.renderInLibrary(sb);
            } else if (!viewingUpgrade) {
                AbstractCard copy = makeStatEquivalentCopy();
                copy.current_x = this.current_x;
                copy.current_y = this.current_y;
                copy.drawScale = this.drawScale;
                if (copy instanceof AbstractNesCard)
                    ((AbstractNesCard) copy).enchant();
                copy.render(sb);
            } else {
                AbstractCard copy = makeStatEquivalentCopy();
                copy.current_x = this.current_x;
                copy.current_y = this.current_y;
                copy.drawScale = this.drawScale;
                copy.upgrade();
                copy.displayUpgrades();
                if (copy instanceof AbstractNesCard)
                    ((AbstractNesCard) copy).enchant();
                copy.render(sb);
            }
            return;
        }
        super.renderInLibrary(sb);
    }
    
    private boolean isOnScreen() {
        return !(current_y < scale(-200F)) && !(current_y > Settings.HEIGHT + scale(200F));
    }

    private void setInitialCardName(String name) {
        this.name = name;
        initializeTitle();
    }

    private void setInitialRawDesc(String rawDesc) {
        this.rawDescription = rawDesc;
        initializeDescription();
    }
    
    private void initTags() {
        if (this instanceof AbstractOffering)
            if (!hasTag(NesCardEnum.OFFERING))
                tags.add(NesCardEnum.OFFERING);
    }

    public void setCanTriggerEnchantment(Predicate<AbstractCard> canTriggerEnchantment) {
        this.canTriggerEnchantment = canTriggerEnchantment;
    }

    public boolean isEnchanted() {
        return enchanted;
    }

    public int getExMagicNum() {
        return ExMagicNum;
    }

    public int getBaseExMagicNum() {
        return baseExMagicNum;
    }

    public void setExMagicNumModified(boolean exMagicNumModified) {
        isExMagicNumModified = exMagicNumModified;
    }

    public void setUpgradedExMagicNum(boolean upgradedExMagicNum) {
        this.upgradedExMagicNum = upgradedExMagicNum;
    }

    public boolean isExMagicNumModified() {
        return isExMagicNumModified;
    }

    public boolean isUpgradedExMagicNum() {
        return upgradedExMagicNum;
    }

    public int getWitherNum() {
        return WitherNum;
    }

    public int getBaseWitherNum() {
        return baseWitherNum;
    }

    public void setWitherNumModified(boolean witherNumModified) {
        isWitherNumModified = witherNumModified;
    }

    public void setUpgradedWitherNum(boolean upgradedWitherNum) {
        this.upgradedWitherNum = upgradedWitherNum;
    }

    public boolean isWitherNumModified() {
        return isWitherNumModified;
    }

    public boolean isUpgradedWitherNum() {
        return upgradedWitherNum;
    }

    public void setWitherNumValue(int num, boolean ignoreModifier) {
        if (!ignoreModifier) {
            if (isWitherNumModified || upgradedWitherNum) return;
        }
        WitherNum = baseWitherNum = num;
        if (baseWitherNum < 0) baseWitherNum = 0;
    }

    public void setExMagicNumValue(int num, boolean ignoreModifier) {
        if (!ignoreModifier) {
            if (isExMagicNumModified || upgradedExMagicNum) return;
        }
        ExMagicNum = baseExMagicNum = num;
        if (baseExMagicNum < 0) baseExMagicNum = 0;
    }
    
    public boolean canNourishCard(@NotNull AbstractCard card) {
        return this.costForTurn > card.costForTurn && !hasCompletelyWithered();
    }
    
    public boolean hasCompletelyWithered() {
        boolean withered = WitherNum <= 0;
        if (isWitherNumModified && WitherNum != baseWitherNum)
            withered = (baseWitherNum <= 0 && WitherNum > 0) == (WitherNum <= 0);
        return withered;
    }
    
    public void reduceWither() {
        if (WitherNum != baseWitherNum && isWitherNumModified) {
            if (WitherNum > 0 && baseWitherNum <= 0)
                WitherNum = baseWitherNum;
            else if (WitherNum <= 0 && baseWitherNum > 0) {
                baseWitherNum--;
                WitherNum = baseWitherNum;
            }
        } else {
            baseWitherNum--;
            WitherNum = baseWitherNum;
        }
        checkIfHasWithered();
    }
    
    protected void checkIfHasWithered() {
        if (hasCompletelyWithered())
            modifyWitheredDescription();
    }
    
    public void modifyWitheredDescription() {
        rawDescription = checkWithPatterns(rawDescription);
        rawDescription = new PatternConstructor(WITHERABLE_TEXT + "((\\s?)\\((\\s?)(!" + NesWitherNum.key + "!)(\\s?)\\))?")
                .compile(Pattern.MULTILINE | Pattern.CASE_INSENSITIVE)
                .matchAndReplace(rawDescription, WITHERED_TEXT);
        initializeDescription();
    }
    
    public void setContentableOffer(boolean contented) {
        if (contented) {
            addTip(CONTENTABLE_HEAD, CONTENTABLE_BODY);
        } else {
            removeTip(CONTENTABLE_HEAD, CONTENTABLE_BODY);
        }
    }
    
    public void modifyContentedTip(@NotNull AbstractCard... card) {
        StringBuilder names = new StringBuilder(card[0].name);
        if (card.length > 1) {
            names.append(", ");
            for (int i = 1; i < card.length; i++) {
                names.append(i == card.length - 1 ? "" : ", ")
                        .append(card[i].name);
            }
        }
        String body = new PatternConstructor("\\b(?<=\\[)(OFFERING(S)?)(?=\\])")
                .compile(Pattern.MULTILINE | Pattern.CASE_INSENSITIVE)
                .matchAndReplace(CONTENTED_BODY, names.toString());
        removeTip(CONTENTABLE_HEAD, CONTENTABLE_BODY);
        addTip(CONTENTED_HEAD, body);
    }

    public void modifyContentedTip(@NotNull List<AbstractCard> card) {
        StringBuilder names = new StringBuilder(card.get(0).name);
        if (card.size() > 1) {
            names.append(", ");
            for (int i = 1; i < card.size(); i++) {
                names.append(i == card.size() - 1 ? "" : ", ")
                        .append(card.get(i).name);
            }
        }
        String body = new PatternConstructor("\\b(?<=\\[)(OFFERING(S)?)(?=\\])")
                .compile(Pattern.MULTILINE | Pattern.CASE_INSENSITIVE)
                .matchAndReplace(CONTENTED_BODY, names.toString());
        removeTip(CONTENTABLE_HEAD, CONTENTABLE_BODY);
        addTip(CONTENTED_HEAD, body);
    }
    
    protected String checkWithPatterns(String str) {
        str = PatternMgr.useExMagicPattern(str, NesExMagicNum.key);
        str = NesPatterMgr.useWitherPattern(str, NesWitherNum.key);
        return str;
    }
    
    protected void Log(Object what) {
        NesDebug.Log(this, this.name + ": " + what);
    }
    
    protected boolean isCardOfferable(AbstractCard card) {
        return !OfferHelper.UnofferableCards.contains(card);
    }

    @Override
    public int returnPrefabUniqueID() {
        return returnNesCardPrefabID(this);
    }

    @Override
    public void onPlay(AbstractCreature s, AbstractCreature t) {
        play(s, t);
        if (enchanted) {
            addToBot(new DelayAction(this::disenchant));
        }
        if (s == cpr() && cpr().hand.size() >= 1) {
            addToBot(new QuickAction(AbstractGameAction.ActionType.CARD_MANIPULATION)
                    .setAction(() -> {
                        int avals = countSpecificCards(cpr().hand, this::canNourishCard);
                        if (avals >= 1) {
                            List<AbstractCard> tmp = new ArrayList<>();
                            cpr().hand.group.forEach(c -> {
                                if (this.canNourishCard(c) && !tmp.contains(c)) tmp.add(c);
                            });
                            if (tmp.isEmpty())
                                Log("Unable to find any card to be nourished, but there shall be " + avals + " cards available");
                            else {
                                int count = tmp.size();
                                for (int i = 0; i < count; i++) {
                                    Optional<AbstractCard> card = getExptRandomCard(cardRandomRng(),
                                            tmp::contains, cpr().hand.group);
                                    card.ifPresent(c -> {
                                        tmp.remove(c);
                                        addToTop(new ReinforceCardDamageAction(c, 1));
                                        addToTop(new ReinforceCardBlockAction(c, 1));
                                        addToTop(new ReinforceCardMagicAction(c, 1));
                                        if (c instanceof AbstractNesCard)
                                            addToTop(new ReinforceCardExMagicAction((AbstractNesCard) c, 1));
                                        reduceWither();
                                        if (hasCompletelyWithered()) {
                                            addToBot(new DelayAction(() -> {
                                                upgradeBaseCost(0);
                                                if (baseDamage > 0) upgradeDamage(-baseDamage / 2);
                                                if (baseBlock > 0) upgradeBlock(-baseBlock / 2);
                                                if (baseMagicNumber > 0) upgradeMagicNumber(-baseMagicNumber / 2);
                                                if (baseExMagicNum > 0) upgradeExMagicNum(-baseExMagicNum / 2);
                                            }));
                                        }
                                    });
                                    if (hasCompletelyWithered()) break;
                                }
                            }
                            tmp.clear();
                        }
                    }));
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractNesCard card = (AbstractNesCard) super.makeStatEquivalentCopy();
        card.oVs = this.oVs;
        card.tVs = this.tVs;
        card.increments = this.increments;
        if (enchanted)
            card.restoreEnchantmentOnSL();
        return card;
    }

    @Override
    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedExMagicNum) {
            ExMagicNum = baseExMagicNum;
            isExMagicNumModified = true;
        }
        if (upgradedWitherNum) {
            WitherNum = baseWitherNum;
            isWitherNumModified = true;
        }
    }

    @Override
    public void promote() {
        if (canUpgrade()) {
            upgradeName();
            upgradeDescription();
            upgradeNewCost();
            upgradeDamage();
            upgradeBlock();
            upgradeMagics();
            upgradeExMagicNum();
            upgradeWitherNum();
            upgradeAttributes();
        }
    }

    protected void upgradeExMagicNum() {
        int v = 0;
        if (data().getCustom("prmtexmagicnum") != null)
            v = Integer.parseInt(data().getCustom("prmtexmagicnum"));
        boolean diff = baseExMagicNum != baseExMagicNum + v;
        if (diff) upgradeExMagicNum(v);
    }

    protected void upgradeExMagicNum(int amount) {
        baseExMagicNum += amount;
        ExMagicNum = baseExMagicNum;
        upgradedExMagicNum = true;
    }

    protected void upgradeWitherNum() {
        int v = 0;
        if (data().getCustom("prmtwithernum") != null)
            v = Integer.parseInt(data().getCustom("prmtwithernum"));
        boolean diff = baseWitherNum != baseWitherNum + v;
        if (diff) upgradeWitherNum(v);
    }

    protected void upgradeWitherNum(int amount) {
        baseWitherNum += amount;
        WitherNum = baseWitherNum;
        upgradedWitherNum = true;
    }

    @Override
    protected void upgradeName() {
        upgradeName(TRUENAME);
    }

    protected void upgradeName(String newName) {
        timesUpgraded++;
        upgraded = true;
        name = newName;
        initializeTitle();
    }

    protected void upgradeDescription() {
        upgradeDescription(UPGRADE_DESCRIPTION);
    }

    protected void upgradeDescription(String newDesc) {
        rawDescription = enchantDesc == null ? newDesc : newDesc + enchantDesc;
        initializeDescription();
    }

    protected void upgradePotrait(String newImg) {
        textureImg = newImg;
        assetUrl = newImg;
        loadCardImage(textureImg);
    }
    
    public boolean canTriggerEnchantedEffect() {
        return isEnchanted() && canTriggerEnchantment.test(this);
    }
    
    public boolean canEnchant() {
        return !enchanted;
    }
    
    public final void enchant() {
        if (canEnchant()) {
            enchanted = true;
            superFlash(Color.ORANGE.cpy());
            spectralize();
            glowColor = Color.ORANGE.cpy();
        }
    }
    
    private void restoreEnchantmentOnSL() {
        enchanted = true;
        superFlash(Color.ORANGE.cpy());
        spectralize();
        glowColor = Color.ORANGE.cpy();
    }
    
    public final void disenchant() {
        if (isEnchanted()) {
            if (cpr().hasPower(EidolonFormPower.POWER_ID)) return;
            enchanted = false;
            despectralize();
            glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
    
    protected void enchantMagics(int slot, int targetValue) {
        oVs[slot] = baseMagicNumber;
        tVs[slot] = tVs[slot] == -99 ? targetValue : tVs[slot];
        increments[slot] = Math.max(targetValue - baseMagicNumber, 0);
        if (increments[slot] > 0 && baseMagicNumber + increments[slot] <= tVs[slot])
            upgradeMagicNumber(increments[slot]);
    }
    
    protected void disenchantMagics(int slot) {
        int decrement = baseMagicNumber - oVs[slot];
        if (decrement < 0) return;
        boolean hasOffset = decrement != increments[slot];
        if (hasOffset) {
            baseMagicNumber -= increments[slot];
            magicNumber = baseMagicNumber;
            upgradedMagicNumber = baseMagicNumber > oVs[slot];
            isMagicNumberModified = baseMagicNumber != oVs[slot];
        } else {
            setMagicValue(oVs[slot], true);
        }
    }

    protected void enchantDamage(int slot, int targetValue) {
        oVs[slot] = baseDamage;
        tVs[slot] = tVs[slot] == -99 ? targetValue : tVs[slot];
        increments[slot] = Math.max(targetValue - baseDamage, 0);
        if (increments[slot] > 0 && baseDamage + increments[slot] <= tVs[slot])
            upgradeDamage(increments[slot]);
    }

    protected void disenchantDamage(int slot) {
        int decrement = baseDamage - oVs[slot];
        if (decrement < 0) return;
        boolean hasOffset = decrement != increments[slot];
        if (hasOffset) {
            baseDamage -= increments[slot];
            damage = baseDamage;
            upgradedDamage = baseDamage > oVs[slot];
            isDamageModified = baseDamage != oVs[slot];
        } else {
            setDamageValue(oVs[slot], true);
        }
    }

    protected void enchantBlock(int slot, int targetValue) {
        oVs[slot] = baseBlock;
        tVs[slot] = tVs[slot] == -99 ? targetValue : tVs[slot];
        increments[slot] = targetValue - baseBlock;
        if (baseBlock + increments[slot] <= tVs[slot])
            upgradeBlock(increments[slot]);
    }

    protected void disenchantBlock(int slot) {
        int decrement = baseBlock - oVs[slot];
        boolean hasOffset = decrement != increments[slot];
        if (hasOffset) {
            baseBlock -= increments[slot];
            block = baseBlock;
            upgradedBlock = baseBlock > oVs[slot];
            isBlockModified = baseBlock != oVs[slot];
        } else {
            setBlockValue(oVs[slot], true);
        }
    }

    protected void enchantExMagics(int slot, int targetValue) {
        oVs[slot] = baseExMagicNum;
        tVs[slot] = tVs[slot] == -99 ? targetValue : tVs[slot];
        increments[slot] = Math.max(targetValue - baseExMagicNum, 0);
        if (increments[slot] > 0 && baseExMagicNum + increments[slot] <= tVs[slot])
            upgradeExMagicNum(increments[slot]);
    }

    protected void disenchantExMagics(int slot) {
        int decrement = baseBlock - oVs[slot];
        if (decrement < 0) return;
        boolean hasOffset = decrement != increments[slot];
        if (hasOffset) {
            baseExMagicNum -= increments[slot];
            ExMagicNum = baseExMagicNum;
            upgradedExMagicNum = baseExMagicNum > oVs[slot];
            isExMagicNumModified = baseExMagicNum != oVs[slot];
        } else {
            setBlockValue(oVs[slot], true);
        }
    }
    
    protected void appendDescription(String appendix) {
        enchantDesc = appendix;
        rawDescription = rawDescription + enchantDesc;
        initializeDescription();
    }
    
    protected void subtractDescription(String subtracts) {
        subtracts = checkWithPatterns(subtracts);
        if (rawDescription.contains(subtracts))
            rawDescription = rawDescription.replace(subtracts, "");
        initializeDescription();
    }
    
    protected abstract void play(AbstractCreature s, AbstractCreature t);
    protected abstract void spectralize();
    protected abstract void despectralize();
    protected abstract String getKey();
    @NotNull
    @Override
    protected String getImageUrl() {
        return NesSK.GetNesCardImage(getKey(), NesFab.rmPrefix(cardID), 0);
    }
    @NotNull
    public abstract Color getFavoriteColor();
}