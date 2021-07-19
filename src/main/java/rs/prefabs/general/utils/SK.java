package rs.prefabs.general.utils;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SK {
    
    public static boolean HasAnyExptCreatures(Predicate<AbstractCreature> expt) {
        List<AbstractCreature> tmp = new ArrayList<>();
        tmp.add(GetPlayer());
        tmp.addAll(GetAllExptMstr(m -> true));
        return tmp.stream().anyMatch(expt);
    }
    
    public static boolean HasAnyExptMonster(Predicate<AbstractMonster> expt) {
        return AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> !m.isDeadOrEscaped() && expt.test(m));
    }

    @NotNull
    public static List<AbstractMonster> GetAllExptMstr(Predicate<AbstractMonster> expt) {
        List<AbstractMonster> tmp = new ArrayList<>();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && expt.test(m))
                tmp.add(m);
        }
        return tmp;
    }

    @NotNull
    public static Optional<AbstractMonster> GetExptMstr(Predicate<AbstractMonster> expt) {
        return AbstractDungeon.getMonsters().monsters.stream().filter(m -> !m.isDeadOrEscaped() && expt.test(m)).findAny();
    }

    @NotNull
    @SafeVarargs
    public static List<AbstractCard> GetAllExptCards(Predicate<AbstractCard> expt, @NotNull List<AbstractCard>... lists) {
        List<AbstractCard> cards = new ArrayList<>();
        for (List<AbstractCard> list : lists) cards.addAll(list);
        cards.removeIf(c -> !expt.test(c));
        return cards;
    }

    @NotNull
    @SafeVarargs
    public static Optional<AbstractCard> GetExptCard(Predicate<AbstractCard> expt, @NotNull List<AbstractCard>... lists) {
        List<AbstractCard> cards = new ArrayList<>();
        for (List<AbstractCard> list : lists) cards.addAll(list);
        cards.removeIf(c -> !expt.test(c));
        return cards.stream().findAny();
    }

    public static int AscnLevel() {
        return AbstractDungeon.ascensionLevel;
    }

    public static void AddToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public static void AddToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public static boolean RandomBool(@NotNull Random rng, float chance) {
        return rng.randomBoolean(chance);
    }

    public static boolean RandomBool(@NotNull Random rng) {
        return rng.randomBoolean();
    }

    public static Random MonsterRng() {
        return AbstractDungeon.monsterRng;
    }

    public static AbstractPlayer GetPlayer() {
        return AbstractDungeon.player;
    }
}