package rs.prefabs.nemesis;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.Keyword;
import rs.prefabs.general.PrefabMgr;
import rs.prefabs.general.interfaces.*;
import rs.prefabs.general.utils.AtkEffectMgr;
import rs.prefabs.general.utils.GeneralUtils;
import rs.prefabs.nemesis.character.NCP;
import rs.prefabs.nemesis.character.NRP;
import rs.prefabs.nemesis.character.OfferHelper;
import rs.prefabs.nemesis.character.TheNemesis;
import rs.prefabs.nemesis.dynamics.NesExMagicNum;
import rs.prefabs.nemesis.dynamics.NesWitherNum;
import rs.prefabs.nemesis.localizations.NesLocalizedStrings;
import rs.prefabs.nemesis.patches.NesCardEnum;
import rs.prefabs.nemesis.patches.NesCustomEnum;
import rs.prefabs.nemesis.utils.NesImageMst;
import rs.prefabs.nemesis.utils.Nesinitializer;

import java.util.Map;
import java.util.Properties;

public class NesFab implements GeneralUtils, EditCharactersSubscriber, EditCardsSubscriber, EditRelicsSubscriber, 
        RegisterCustomCardAttrsSubscriber, StartGameSubscriber, PostInitializeSubscriber, OnInitializeSubscriber, 
        EditStringsSubscriber, EditKeywordsSubscriber, AddAudioSubscriber, EndTurnPreDiscardSubscriber, PostBattleSubscriber, 
        OnCardPlayedSubscriber, PostTurnStartSubscriber {
    private static NesLocalizedStrings nemesispack;
    private static final String PREFIX = "nemesismod:";
    private static final String NEMESIS_BTN = "PrefabsAssets/NemesisProperties/images/charSets/nemesisButton.png";
    private static final String NEMESIS_PTR = "PrefabsAssets/NemesisProperties/images/charSets/nemesisPortrait.jpg";
    public static final Color NesColor = Color.LIME.cpy();
    
    public static final int[] CardTrack = new int[200];

    public static void initialize() {
        Nesinitializer.preInit();
        NesFab fab = new NesFab();
        initProps();
    }
    
    public NesFab() {
        Prefabscriber.subscribe(this);
        BaseMod.subscribe(this);
        addNewColor();
    }
    
    public static void initProps() {
        try {
            Properties props = new Properties();
            for (int i = 0; i < CardTrack.length; i++) {
                if (i == 0) {
                    props.setProperty("CardTrackPreserved", "0");
                    continue;
                }
                props.setProperty("CardTrack#" + i, i <= 75 ? "1" : "0");
            }
            SpireConfig config = new SpireConfig("[Prefab]Nemesis", "Tracks", props);
            for (int i = 0; i < CardTrack.length; i++) {
                if (i == 0) {
                    CardTrack[i] = 0;
                    continue;
                }
                CardTrack[i] = config.getInt("CardTrack#" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void saveCurrData() {
        try {
            saveData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void saveData() throws Exception {
        SpireConfig config = new SpireConfig("[Prefab]Nemesis", "Tracks");
        for (int i = 0; i < CardTrack.length; i++) {
            if (i == 0) {
                config.setInt("CardTrackPreserved", 0);
                continue;
            }
            config.setInt("CardTrack#" + i, CardTrack[i]);
        }
    }

    public static NesLocalizedStrings getNemesispack() {
        return nemesispack;
    }

    @NotNull
    @Contract(pure = true)
    public static String makeID(String id) {
        return makeID(id, PREFIX);
    }

    @NotNull
    @Contract(pure = true)
    public static String makeID(String id, String prefix) {
        return prefix + id;
    }

    public static String rmPrefix(String id) {
        return rmPrefix(id, PREFIX);
    }

    public static String rmPrefix(@NotNull String id, String prefix) {
        return id.contains(prefix) ? id.replace(prefix, "") : id;
    }

    @Override
    public void receiveOnInitialize() {
        nemesispack = new NesLocalizedStrings();
    }
    
    protected void addNewColor() {
        BaseMod.addColor(NesCardEnum.Nemesis, NesColor, NesColor, NesColor, NesColor, NesColor, NesColor, NesColor, 
                "PrefabsAssets/NemesisProperties/images/cardui/general/512/bg_attack.png",
                "PrefabsAssets/NemesisProperties/images/cardui/general/512/bg_skill.png",
                "PrefabsAssets/NemesisProperties/images/cardui/general/512/bg_power.png",
                "PrefabsAssets/NemesisProperties/images/cardui/general/512/cost_orb.png",
                "PrefabsAssets/NemesisProperties/images/cardui/general/1024/bg_attack.png",
                "PrefabsAssets/NemesisProperties/images/cardui/general/1024/bg_skill.png",
                "PrefabsAssets/NemesisProperties/images/cardui/general/1024/bg_power.png",
                "PrefabsAssets/NemesisProperties/images/cardui/general/1024/card_orb.png");
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new TheNemesis(), NEMESIS_BTN, NEMESIS_PTR, TheNemesis.Enum.THE_NEMESIS);
    }

    @Override
    public void receiveEditCards() {
        NCP.AddCards();
        BaseMod.addDynamicVariable(new NesExMagicNum());
        BaseMod.addDynamicVariable(new NesWitherNum());
    }

    @Override
    public void receiveEditRelics() {
        NRP.AddRelics();
    }

    @Override
    public void receiveEditStrings() {
        String lang = getSupportedLanguage(Settings.language);
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "PrefabsAssets/NemesisProperties/localizations/"
                + lang + "/characters.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "PrefabsAssets/NemesisProperties/localizations/"
                + lang + "/ui.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "PrefabsAssets/NemesisProperties/localizations/"
                + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "PrefabsAssets/NemesisProperties/localizations/"
                + lang + "/powers.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class, "PrefabsAssets/NemesisProperties/localizations/"
                + lang + "/monsters.json");
    }

    @Override
    public void receivePreAttrsAssign() {
        PrefabMgr.addCustomRarity("nesepic", NesCardEnum.NESEPIC);
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(NesFab.makeID("Nemesis_Awk"), "PrefabsAssets/NemesisProperties/audio/Awaken_Nemesis.ogg");
        BaseMod.addAudio(NesFab.makeID("Nemesis_Atk_v1"), "PrefabsAssets/NemesisProperties/audio/Nemesis_Attack_v1.ogg");
        BaseMod.addAudio(NesFab.makeID("Nemesis_Atk_v2"), "PrefabsAssets/NemesisProperties/audio/Nemesis_Attack_v2.ogg");
        BaseMod.addAudio(NesFab.makeID("Nullify_EXT"), "PrefabsAssets/GeneralProperties/audio/SFX_Nullify_EXT.ogg");
        BaseMod.addAudio(NesFab.makeID("Psychic_SFX"), "PrefabsAssets/NemesisProperties/audio/SFX_Psychic_v1.ogg");
    }

    @Override
    public void receiveEditKeywords() {
        loadLocKeywords();
    }

    protected void loadLocKeywords() {
        Map<String, Keyword> keywords = Keyword.fromJson("PrefabsAssets/NemesisProperties/localizations/"
                + getSupportedLanguage(Settings.language) + "/keywords.json");

        keywords.forEach((k,v)->{
            NesDebug.Log("Loading nemesis keywords：" + v.NAMES[0]);
            BaseMod.addKeyword(PREFIX, v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receiveStartGame() {
        OfferHelper.initPreBattle();
    }

    @Override
    public void receivePostInitialize() {
        Nesinitializer.postInit();
        AtkEffectMgr.registerEffect(NesCustomEnum.NES_PLAGUE, NesImageMst.NES_PLAGUE_EFFECT, "ATTACK_POISON");
        AtkEffectMgr.registerEffect(NesCustomEnum.NES_DECAY, NesImageMst.NES_DECAY_EFFECT, "BLUNT_FAST");
    }

    @Override
    public void receiveOnEndTurnPreDiscard() {
        
    }

    @Override
    public void receivePostBattle(AbstractRoom room) {
        OfferHelper.clearPostBattle();
    }

    @Override
    public void receiveOnCardPlayed(AbstractCard card, UseCardAction action) {
        OfferHelper.OnCardPlayed(card, action);
    }

    @Override
    public void receivePostTurnStart(AbstractCreature creature, boolean postDraw) {
        OfferHelper.AtStartOfTurn(creature, postDraw);
    }
}