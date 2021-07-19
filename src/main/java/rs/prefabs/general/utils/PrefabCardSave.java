package rs.prefabs.general.utils;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import javassist.CtBehavior;
import rs.prefabs.general.abstracts.AbstractPrefabCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Deprecated
public class PrefabCardSave extends CardSave {
    public PrefabData data;
    
    public PrefabCardSave(String cardID, int timesUpgraded, int misc) {
        this(cardID, timesUpgraded, misc, null);
    }

    public PrefabCardSave(String cardID, int timesUpgraded, int misc, PrefabData data) {
        super(cardID, timesUpgraded, misc);
        this.data = data;
    }
    /*
    public static class CustomCardSavePatch {
        @SpirePatch(clz = CardGroup.class, method = "getCardDeck")
        public static class ReturnCustomSave {
            @SpirePrefixPatch
            public static SpireReturn<ArrayList<CardSave>> returnCustomCardSave(CardGroup _inst) {
                ArrayList<CardSave> cards = new ArrayList<>();
                for (AbstractCard c : _inst.group) {
                    if (c instanceof AbstractPrefabCard) {
                        cards.add(new PrefabCardSave(c.cardID, c.timesUpgraded, c.misc,
                                ((AbstractPrefabCard) c).returnPrefabData()));
                        Debugger.Log("Uploading " + c.name + "'s custom data: " + ((AbstractPrefabCard) c).returnPrefabData().toString());
                    }
                    else 
                        cards.add(new CardSave(c.cardID, c.timesUpgraded, c.misc));
                }
                return SpireReturn.Return(cards);
            }
        }
        @SpirePatch(clz = CardCrawlGame.class, method = "loadPlayerSave")
        public static class RestoreCustomData {
            @SpireInsertPatch(locator = Locator.class)
            public static void Insert(CardCrawlGame _inst, AbstractPlayer p) {
                if (!p.masterDeck.isEmpty() && p.masterDeck.group.stream().anyMatch(c -> c instanceof AbstractPrefabCard)) {
                    List<AbstractCard> cards = new ArrayList<>(p.masterDeck.group);
                    cards.removeIf(c -> !(c instanceof AbstractPrefabCard));
                    for (CardSave save : CardCrawlGame.saveFile.cards) {
                        if (!(save instanceof PrefabCardSave)) continue;
                        if (cards.stream().anyMatch(c -> c.cardID.equals(save.id))) {
                            Optional<AbstractCard> card = p.masterDeck.group.stream()
                                    .filter(c -> c instanceof AbstractPrefabCard && c.cardID.equals(save.id))
                                    .findFirst();
                            if (card.isPresent() && card.get() instanceof AbstractPrefabCard) {
                                if (!((AbstractPrefabCard) card.get()).loadFromSave((PrefabCardSave) save))
                                    Debugger.Log(card.get().name + " failed to load data from save");
                            }
                        }
                    }
                }
            }
            private static class Locator extends SpireInsertLocator {
                @Override
                public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                    Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(Settings.class, "isEndless");
                    return LineFinder.findInOrder(ctMethodToPatch, matcher);
                }
            }
        }
    }*/
}