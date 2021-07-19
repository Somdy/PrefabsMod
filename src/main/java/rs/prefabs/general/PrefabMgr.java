package rs.prefabs.general;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.abstracts.AbstractPrefabCard;
import rs.prefabs.general.data.CustomCardTags;
import rs.prefabs.general.data.CustomRarity;
import rs.prefabs.general.interfaces.*;
import rs.prefabs.general.listeners.ApplyPowerListener;
import rs.prefabs.general.listeners.TurnEventListener;
import rs.prefabs.general.listeners.UseCardListener;
import rs.prefabs.general.utils.Debugger;
import rs.prefabs.general.utils.GeneralUtils;
import rs.prefabs.general.utils.Prefabinitializer;
import rs.prefabs.nemesis.NesFab;

import java.util.Map;

@SpireInitializer
public class PrefabMgr implements GeneralUtils, EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, 
        AddAudioSubscriber, PostDrawSubscriber, MonsterEndTurnSubscriber {
    private static final String PREFIX = "prefabmod:";
    
    public static void initialize() {
        Prefabinitializer.preInit();
        PrefabMgr fab = new PrefabMgr();
        initializePrefabs();
    }
    
    public static void initializePrefabs() {
        NesFab.initialize();
    }
    
    public PrefabMgr() {
        Prefabinitializer.postInit();
    }
    
    public static void addCustomRarity(String key, AbstractCard.CardRarity rarity) {
        CustomRarity.registerRarity(key, rarity);
    }

    public static void addCustomCardTag(String key, AbstractCard.CardTags tag) {
        CustomCardTags.registerTag(key, tag);
    }
    
    public static void cleanAfterJobsDone() {
        System.gc();
    }
    
    protected AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }

    @Override
    public void receiveEditCards() {
        
    }

    @Override
    public void receiveEditRelics() {
        
    }

    @Override
    public void receiveEditStrings() {
        
    }

    @Override
    public void receiveAddAudio() {
        
    }

    private static String supportedLanguage(@NotNull Settings.GameLanguage language) {
        switch(language) {
            case ZHS:
                return "zhs";
            default:
                return "eng";
        }
    }
    
    public static void loadGeneralKeywords() {
        Map<String, Keyword> keywords = Keyword.fromJson("PrefabsAssets/GeneralProperties/localizations/"
                + supportedLanguage(Settings.language) + "/keywords.json");
        keywords.forEach((k,v)->{
            Debugger.Log("Loading general keywords：" + v.NAMES[0]);
            BaseMod.addKeyword(PREFIX, v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receivePostDraw(AbstractCard card) {
        for (AbstractCard c : cpr().hand.group) {
            if (c instanceof AbstractPrefabCard)
                ((AbstractPrefabCard) c).triggerPostCardDrawn(card);
        }
        for (AbstractCard c : cpr().drawPile.group) {
            if (c instanceof AbstractPrefabCard)
                ((AbstractPrefabCard) c).triggerPostCardDrawn(card);
        }
        for (AbstractCard c : cpr().discardPile.group) {
            if (c instanceof AbstractPrefabCard)
                ((AbstractPrefabCard) c).triggerPostCardDrawn(card);
        }
    }
    
    public static void receiveOnTurnStart(AbstractCreature who, boolean postDraw) {
        UseCardListener.updatePostTurnStart();
        TurnEventListener.triggerStartOfTurnEvents(who);
    }
    
    public static void receiveOnStartOfCombat(AbstractRoom r) {
        TurnEventListener.loadAtStartOfCombat(r);
    }

    public static void receiveAtEndOfCombat(AbstractRoom r) {
        ApplyPowerListener.clearPostCombat();
    }
    
    public static void receiveOnPlayerEndsTurn(AbstractPlayer p) {
        TurnEventListener.triggerEndOfTurnEvents(p);
    }

    @Override
    public void receiveOnMonsterTurnEnds(AbstractMonster m) {
        TurnEventListener.triggerEndOfTurnEvents(m);
    }
}