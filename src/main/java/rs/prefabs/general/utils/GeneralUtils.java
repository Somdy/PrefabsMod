package rs.prefabs.general.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

public interface GeneralUtils {

    default boolean isFieldExists(String field, Class<?> type, Object target) {
        if (target == null || StringUtils.isEmpty(field)) {
            return false;
        }
        boolean exists = false;
        for (Field f : getAllFields(target)) {
            f.setAccessible(true);
            if (f.getName().equals(field) && f.getType() == type) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    @NotNull
    default List<Field> getAllFields(@NotNull Object target) {
        List<Field> fields = new ArrayList<>();
        Class clz = target.getClass();
        while (clz != null) {
            fields.addAll(Arrays.asList(clz.getDeclaredFields()));
            clz = clz.getSuperclass();
        }
        return fields;
    }

    default Field getSpecificField(String field, Class<?> type, Object target) {
        if (!isFieldExists(field, type, target)) {
            return null;
        }
        Field retval = null;
        for (Field f : getAllFields(target)) {
            f.setAccessible(true);
            if (f.getName().equals(field) && f.getType() == type) {
                retval = f;
                break;
            }
        }
        return retval;
    }

    default <T> Optional<T> getRandom(@NotNull List<T> list, Random rng) {
        if (list.isEmpty())
            return Optional.empty();
        if (list.size() == 1)
            return Optional.ofNullable(list.get(0));
        int index = rng.random(list.size() - 1);
        return Optional.ofNullable(list.get(index));
    }

    default <T> Optional<T> getRandom(@NotNull List<T> list) {
        if (list.isEmpty())
            return Optional.empty();
        if (list.size() == 1)
            return Optional.ofNullable(list.get(0));
        int index = MathUtils.random(list.size() - 1);
        return Optional.ofNullable(list.get(index));
    }

    default <T extends AbstractCard> Optional<T> getExptRandomCard(Random rng, Predicate<T> expt, @NotNull List<T>... lists) {
        List<T> list = new ArrayList<>();
        for (List<T> cards : lists) {
            list.addAll(cards);
        }
        list.removeIf(t -> !expt.test(t));
        if (list.isEmpty()) return Optional.empty();
        return getRandomCard(list, rng);
    }

    default <T extends AbstractCard> Optional<T> getRandomCard(Random rng, @NotNull List<T>... lists) {
        List<T> list = new ArrayList<>();
        for (List<T> cards : lists) {
            list.addAll(cards);
        }
        return getRandomCard(list, rng);
    }

    default <T extends AbstractCard> Optional<T> getRandomCard(@NotNull List<T> list, Random rng) {
        return getRandom(list, rng);
    }

    default <T extends AbstractCard> Optional<T> getRandomCard(@NotNull List<T> list) {
        return getRandom(list);
    }

    default boolean hasAnyExptCreatures(Predicate<AbstractCreature> expt) {
        return SK.HasAnyExptCreatures(expt);
    }

    default List<AbstractCreature> getAllExptCreatures(Predicate<AbstractCreature> expected) {
        List<AbstractCreature> creatures = new ArrayList<>();
        creatures.add(AbstractDungeon.player);
        creatures.addAll(AbstractDungeon.getMonsters().monsters);
        creatures.removeIf(c -> !expected.test(c));
        return creatures;
    }

    default List<AbstractCreature> getAllLivingCreatures() {
        return getAllExptCreatures(c -> c != null && !c.isDeadOrEscaped());
    }

    default boolean hasAnyExptMstr(Predicate<AbstractMonster> expt) {
        return SK.HasAnyExptMonster(expt);
    }

    default List<AbstractMonster> getAllExptMstrs(Predicate<AbstractCreature> expected) {
        List<AbstractMonster> creatures = new ArrayList<>(AbstractDungeon.getMonsters().monsters);
        creatures.removeIf(c -> !expected.test(c));
        return creatures;
    }

    default List<AbstractMonster> getAllLivingMstrs() {
        return getAllExptMstrs(m -> m != null && !m.isDeadOrEscaped());
    }

    default Optional<AbstractRelic> getExptRelic(Predicate<AbstractRelic> expt) {
        return AbstractDungeon.player.relics.stream().filter(expt).findFirst();
    }

    default <T> ArrayList<T> objsToList(@NotNull T... objects) {
        ArrayList<T> list = new ArrayList<>();
        if (Arrays.stream(objects).noneMatch(Objects::nonNull))
            return list;
        for (T obj : objects) {
            if (obj != null && !list.contains(obj))
                list.add(obj);
        }
        return list;
    }

    default ArrayList<AbstractCard> cardsToList(@NotNull AbstractCard... cards) {
        return objsToList(cards);
    }

    default boolean hasAnyExptCard(Predicate<AbstractCard> expt, @NotNull List<? extends AbstractCard>... lists) {
        List<AbstractCard> tmp = new ArrayList<>();
        for (List<? extends AbstractCard> list : lists) tmp.addAll(list);
        return tmp.stream().anyMatch(expt);
    }

    default boolean isCardRarityOf(@NotNull AbstractCard card, AbstractCard.CardRarity rarity) {
        return card.rarity == rarity;
    }

    default boolean isCardTypeOf(@NotNull AbstractCard card, AbstractCard.CardType type) {
        return card.type == type;
    }

    default boolean isCardTargetOf(AbstractCard card, @NotNull AbstractCard.CardTarget... targets) {
        boolean is = false;
        for (AbstractCard.CardTarget target : targets) {
            if (card.target == target) {
                is = true;
                break;
            }
        }
        return is;
    }

    default boolean isCardCostsMore(AbstractCard card, int cost, boolean equal) {
        if (equal) {
            return card.costForTurn >= cost && (cost == 0 || !card.freeToPlayOnce) || card.cost == -1 && card.energyOnUse >= cost;
        }
        return card.costForTurn > cost && (cost == 0 || !card.freeToPlayOnce) || card.cost == -1 && card.energyOnUse > cost;
    }

    default int getCardRealCost(@NotNull AbstractCard card) {
        return card.freeToPlayOnce ? 0 : (Math.max(card.costForTurn, 0));
    }

    default int countSpecificCards(@NotNull CardGroup group, Predicate<AbstractCard> expected) {
        int count = 0;
        for (AbstractCard card : group.group) {
            if (expected.test(card))
                count++;
        }
        return count;
    }

    default int countSpecificPowerAmount(@NotNull AbstractCreature target, Predicate<AbstractPower> expected) {
        int count = 0;
        if (target.powers.stream().noneMatch(expected))
            return 0;
        for (AbstractPower p : target.powers) {
            if (expected.test(p)) {
                count += p.amount > 0 ? p.amount : 1;
            }
        }
        return count;
    }
    
    default Optional<AbstractPower> getExptPower(AbstractCreature target, Predicate<AbstractPower> expt) {
        return target.powers.stream().filter(expt).findFirst();
    }
    
    default boolean hasAnyPowerOf(@NotNull AbstractCreature target, Predicate<AbstractPower> expt) {
        return !target.powers.isEmpty() && target.powers.stream().anyMatch(expt);
    }

    default boolean isPowerTypeOf(@NotNull AbstractPower p, AbstractPower.PowerType type) {
        return p.type == type;
    }

    default void effectToList(AbstractGameEffect effect) {
        AbstractDungeon.effectList.add(effect);
    }

    default void playSound(String key) {
        CardCrawlGame.sound.play(key);
    }

    default void playAdjustedSound(String key, float pitch) {
        CardCrawlGame.sound.playA(key, pitch);
    }

    default void voidrun(SecurityRunner.SecureRuuner action) {
        voidrun(action, null);
    }

    default void voidrun(SecurityRunner.SecureRuuner action, SecurityRunner.SecureRuuner exceptionAction) {
        SecurityRunner sr = new SecurityRunner<>(null).setActions(action).setExcpActions(exceptionAction);
        sr.execute();
    }

    default boolean boolrun(SecurityRunner.SecureRuuner<Boolean> action) {
        return boolrun(action, null);
    }

    default boolean boolrun(SecurityRunner.SecureRuuner<Boolean> action, SecurityRunner.SecureRuuner<Boolean> exceptionAction) {
        SecurityRunner<Boolean> sr = new SecurityRunner<>(Boolean.FALSE).setActions(action).setExcpActions(exceptionAction);
        return sr.execute();
    }
    
    default boolean isCreatureWounded(@NotNull AbstractCreature crt) {
        return crt.currentHealth < crt.maxHealth && !crt.isDeadOrEscaped();
    }

    default boolean areMstrBasicallyDead() {
        return AbstractDungeon.getMonsters().areMonstersBasicallyDead();
    }

    default int ascenLevel() {
        return AbstractDungeon.ascensionLevel;
    }

    default int currFloor() {
        return AbstractDungeon.floorNum;
    }

    default int currAct() {
        return AbstractDungeon.actNum;
    }

    default AbstractRoom currRoom() {
        return AbstractDungeon.getCurrRoom();
    }

    default Random cardRandomRng() {
        return AbstractDungeon.cardRandomRng;
    }
    
    default Random relicRng() {
        return AbstractDungeon.relicRng;
    }

    default Random monsterRng() {
        return AbstractDungeon.monsterRng;
    }

    default Random monsterAiRng() {
        return AbstractDungeon.aiRng;
    }

    default Random miscRng() {
        return AbstractDungeon.miscRng;
    }

    default Color quickColor(int r, int g, int b) {
        return quickColor(r, g, b, 255);
    }

    default Color quickColor(int r, int g, int b, int a) {
        return quickColor(r, g, b, a / 255F);
    }

    default Color quickColor(int r, int g, int b, float a) {
        return quickColor(r * 1F, g * 1F, b * 1F, a);
    }

    default Color quickColor(float r, float g, float b, float a) {
        return new Color(r / 255F, g / 255F, b / 255F, a);
    }

    default Color complement(@NotNull Color color) {
        float r = color.r * 255F;
        float g = color.g * 255F;
        float b = color.b * 255F;
        return quickColor(255F - r, 255F - g, 255F - b, color.a);
    }

    default Color contrast(@NotNull Color color) {
        return contrast(color, 127.5F);
    }

    default Color contrast(@NotNull Color color, float deg) {
        if (deg >= 255F)
            return complement(color);
        float r = color.r * 255F;
        float g = color.g * 255F;
        float b = color.b * 255F;
        return quickColor(Math.abs(deg - r), Math.abs(deg - g), Math.abs(deg - b), color.a);
    }

    default Color blend(@NotNull Color c1, @NotNull Color c2) {
        float[] value1 = new float[] {c1.r, c1.g, c1.b};
        float[] value2 = new float[] {c2.r, c2.g, c2.b};
        float[] powers = new float[] {c1.a, c2.a};
        float a = calculateBlendPower(powers[0], powers[1]);
        float r = calculateBlendChannel(powers[0], powers[1], value1[0], value2[0]);
        float g = calculateBlendChannel(powers[0], powers[1], value1[1], value2[1]);
        float b = calculateBlendChannel(powers[0], powers[1], value1[2], value2[2]);
        return quickColor(r, g, b, a);
    }

    default float calculateBlendPower(float a1, float a2) {
        float value = a1 + a2 - a1 * a2;
        return value <= 1 ? value : 1;
    }

    default float calculateBlendChannel(float a1, float a2, float c1, float c2) {
        return (c1 * a1 * (1F - a2) + c2 * a2) / (calculateBlendPower(a1, a2));
    }

    default float scale(float origin) {
        return origin * Settings.scale;
    }

    default String getSupportedLanguage(@NotNull Settings.GameLanguage language) {
        switch(language) {
            case ZHS:
                return "zhs";
            default:
                return "eng";
        }
    }

    default double SciRound(double a, double reserved) {
        return MathKits.SciRound(a, reserved);
    }

    default float SciRound(double a) {
        return (float) MathKits.SciRound(a, 0);
    }

    default long SciPercent(double a) {
        return MathKits.SciPercent(a);
    }

    default boolean BoxInteractsCircle(@NotNull Hitbox box, @NotNull Vector2 o, float r) {
        return MathKits.BoxInteractsCircle(box, o, r);
    }
}