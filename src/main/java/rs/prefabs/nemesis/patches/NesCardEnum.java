package rs.prefabs.nemesis.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class NesCardEnum {
    @SpireEnum(name = "Nemesis")
    public static AbstractCard.CardColor Nemesis;
    @SpireEnum(name = "Nemesis")
    public static CardLibrary.LibraryType NesCard;
    @SpireEnum(name = "NesEpic")
    public static AbstractCard.CardRarity NESEPIC;
    
    @SpireEnum(name = "OfferingItem")
    public static AbstractCard.CardTags OFFERING;
}