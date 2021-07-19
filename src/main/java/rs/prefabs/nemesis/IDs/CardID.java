package rs.prefabs.nemesis.IDs;

import java.lang.reflect.Field;

public final class CardID {
    public static Field[] IDs;
    public static final int FlashStrike = 1;
    public static final int IllusiveShield = 2;
    public static final int Scorch = 3;
    public static final int Metamorphose = 4;
    public static final int SurrealMemoir = 5;
    public static final int DreadCome = 6;
    public static final int Cultivate = 7;
    public static final int DivineWrath = 8;
    public static final int Vanish = 9;
    public static final int CoercedGiving = 10;
    public static final int FoulBlood = 11;
    public static final int TaintedPotion = 12;
    public static final int MindRot = 13;
    public static final int Ghoscythe_Bouncing = 14;
    public static final int Weirnelon = 15;
    public static final int Firebolt = 16;
    public static final int IncendiaryDelight = 17;
    public static final int TraitorCloak = 18;
    public static final int EvilCollar = 19;
    public static final int GateOfFire = 20;
    public static final int PhantasmicMark = 21;
    //public static final int Spectralize = 22;
    public static final int BloodyBlades = 22;
    public static final int OfferInFlesh = 23;
    public static final int PretendToEscape = 24;
    public static final int BloodySlaughter = 25;
    public static final int SearingSaw = 26;
    public static final int LostPuppet = 27;
    public static final int StubbornDefense = 28;
    public static final int SpectralDeluge = 29;
    public static final int PourOnFlames = 30;
    public static final int CripplingFears = 31;
    public static final int HauntingVision = 32;
    public static final int BitterRevelation = 33;
    public static final int MindDrain = 34;
    public static final int Warlike = 35;
    public static final int ScorchingHeat = 36;
    public static final int RottenInside = 37;
    public static final int Overhedge = 38;
    public static final int CastawayWeapon = 39;
    public static final int RunicCollar = 40;
    public static final int EtherealPain = 41;
    public static final int BoundInDeath = 42;
    public static final int DeathInvoker = 43;
    public static final int LeafOnWater = 44;
    public static final int DirtyBlood = 45;
    public static final int Overflow = 46;
    public static final int Ghoscythe_MagicAbsorb = 47;
    public static final int GateOfForge = 48;
    public static final int SiphonEnergy = 49;
    public static final int GoldenApple = 50;
    public static final int WickedMask = 51;
    public static final int Cacophony = 52;
    public static final int Carrion = 53;
    public static final int Ghosting = 54;
    public static final int TwistReality = 55;
    public static final int ErraticPhantasm = 56;
    public static final int SelfishDevotion = 57;
    public static final int TemporalIsolation = 58;
    public static final int EidolonForm = 59;
    public static final int NamesOfGods = 60;
    public static final int RustyObelish = 61;
    public static final int CorruptorDisguise = 62;
    public static final int ColdBlooded = 63;
    public static final int RuleOfSacrifice = 64;
    public static final int ExileToTheVoid = 65;
    public static final int SpectralCorporeity = 66;
    public static final int Desperation = 67;
    public static final int OfferingManagement = 68;
    public static final int Impatience = 69;
    public static final int Ghoscythe_Cursed = 70;
    
    public static final int RottenBone = 180;
    public static final int Multinstances = 181;
    
    public static void initialize() {
        IDs = CardID.class.getDeclaredFields();
    }
}