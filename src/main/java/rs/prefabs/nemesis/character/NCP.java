package rs.prefabs.nemesis.character;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.interfaces.TemplateItem;
import rs.prefabs.nemesis.IDs.CardID;
import rs.prefabs.nemesis.NesDebug;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.cards.AbstractNesCard;
import rs.prefabs.nemesis.cards.generals.*;
import rs.prefabs.nemesis.cards.temps.Multinstances;
import rs.prefabs.nemesis.cards.temps.RottenBone;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class NCP {
    private static List<AbstractNesCard> generals;
    private static List<AbstractNesCard> temps;
    private static List<AbstractNesCard> unis;

    private static int[] Witherables;
    private static int[] Ghoscythes;
    private static int[] Ascenders;
    
    public static void initialize() {
        generals = new ArrayList<>();
        temps = new ArrayList<>();
        unis = new ArrayList<>();

        Witherables = new int[] {
                CardID.DivineWrath, CardID.CastawayWeapon, CardID.CorruptorDisguise
        };
        Ghoscythes = new int[] {
                CardID.Ghoscythe_Bouncing, CardID.Ghoscythe_MagicAbsorb, CardID.Ghoscythe_Cursed
        };
        Ascenders = new int[] {
                CardID.TraitorCloak, CardID.CastawayWeapon, CardID.CorruptorDisguise
        };
    }
    
    public static void AddCards() {
        final int[] id = {-1};

        //Adding starting cards
        generals.add(new FlashStrike());
        generals.add(new IllusiveShield());
        generals.add(new Scorch());
        generals.add(new Metamorphose());
        generals.add(new SurrealMemoir());
        //Adding common cards
        generals.add(new DreadCome());
        generals.add(new Cultivate());
        generals.add(new DivineWrath());
        generals.add(new Vanish());
        generals.add(new CoercedGiving());
        generals.add(new FoulBlood());
        generals.add(new TaintedPotion());
        generals.add(new MindRot());
        generals.add(new Ghoscythe_Bouncing());
        generals.add(new Weirnelon());
        generals.add(new Firebolt());
        generals.add(new IncendiaryDelight());
        generals.add(new TraitorCloak());
        generals.add(new EvilCollar());
        generals.add(new GateOfFire());
        generals.add(new OfferingManagement());
        generals.add(new Ghoscythe_Cursed());
        //Adding uncommon cards
        generals.add(new PhantasmicMark());
        //generals.add(new Spectralize());
        generals.add(new BloodyBlades());
        generals.add(new OfferInFlesh());
        generals.add(new PretendToEscape());
        generals.add(new BloodySlaughter());
        generals.add(new SearingSaw());
        //generals.add(new Ghoscythe_Scrape());
        generals.add(new LostPuppet());
        generals.add(new StubbornDefense());
        generals.add(new PourOnFlames());
        generals.add(new CripplingFears());
        generals.add(new HauntingVision());
        generals.add(new BitterRevelation());
        generals.add(new MindDrain());
        generals.add(new Warlike());
        generals.add(new ScorchingHeat());
        generals.add(new RottenInside());
        generals.add(new Overhedge());
        generals.add(new CastawayWeapon());
        generals.add(new RunicCollar());
        generals.add(new EtherealPain());
        generals.add(new BoundInDeath());
        generals.add(new DeathInvoker());
        //generals.add(new DoomStrike());
        generals.add(new LeafOnWater());
        //generals.add(new Muhahahahaha());
        generals.add(new DirtyBlood());
        //generals.add(new SwiftSlash());
        generals.add(new Overflow());
        generals.add(new Ghoscythe_MagicAbsorb());
        generals.add(new GateOfForge());
        //generals.add(new EnergyConversion());
        generals.add(new SiphonEnergy());
        generals.add(new GoldenApple());
        generals.add(new WickedMask());
        generals.add(new Cacophony());
        generals.add(new Carrion());
        generals.add(new Ghosting());
        generals.add(new TwistReality());
        generals.add(new SpectralCorporeity());
        generals.add(new Desperation());
        generals.add(new Impatience());
        //Adding rare cards
        generals.add(new ErraticPhantasm());
        generals.add(new SelfishDevotion());
        generals.add(new TemporalIsolation());
        generals.add(new EidolonForm());
        generals.add(new NamesOfGods());
        generals.add(new RustyObelish());
        generals.add(new CorruptorDisguise());
        generals.add(new ColdBlooded());
        generals.add(new RuleOfSacrifice());
        generals.add(new ExileToTheVoid());
        generals.add(new SpectralDeluge()); // Moved from uncommon
        //TODO: Need an array to sort all of cards
        
        temps.add(new RottenBone());
        temps.add(new Multinstances());
        
        generals.forEach(card -> {
            if (card.getPrefabid() != id[0])
                id[0] = card.getPrefabid();
            else if (card.getPrefabid() == id[0] && card.getPrefabid() > 0) {
                NesDebug.Log("Detecting repeated id = " + id[0] + ", from card: " + card.name);
                NesDebug.Log("Go yell at the author there's a BIG BUG!!!");
            }
            if (NesFab.CardTrack[id[0]] > 0) {
                BaseMod.addCard(card.makeStatEquivalentCopy());
                NesDebug.Log("Adding card: " + id[0] + " " + card.name);
                UnlockTracker.unlockCard(card.cardID);
            }
        });
        
        unis.addAll(generals);
        unis.addAll(temps);
    }
    
    public static Optional<AbstractCard> GetCard(int id) {
        Optional<AbstractNesCard> targetCard = unis.stream().filter(c -> c.getPrefabid() == id).findFirst();
        return targetCard.map(AbstractNesCard::makeStatEquivalentCopy);
    }
    
    @NotNull
    public static List<AbstractCard> GetCards(Predicate<AbstractCard> expt) {
        List<AbstractCard> tmp = new ArrayList<>();
        unis.stream()
                .filter(c -> NesFab.CardTrack[c.getPrefabid()] > 0 && expt.test(c))
                .forEach(c -> tmp.add(c.makeStatEquivalentCopy()));
        return tmp;
    }
    
    @NotNull
    public static List<AbstractNesCard> GetAllCards() {
        List<AbstractNesCard> tmp = new ArrayList<>();
        unis.forEach(c -> tmp.add((AbstractNesCard) c.makeStatEquivalentCopy()));
        return tmp;
    }
}