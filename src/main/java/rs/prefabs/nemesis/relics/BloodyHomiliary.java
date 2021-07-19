package rs.prefabs.nemesis.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.prefabs.general.cards.PrefabCardTags;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.enums.NesRelicFamily;
import rs.prefabs.nemesis.patches.NesCardEnum;

public class BloodyHomiliary extends AbstractNesRelic {
    public static final String ID = NesFab.makeID("BloodyHomiliary");
    public static float HealEffective = 0.25F;
    
    public BloodyHomiliary() {
        super(ID, NesRelicFamily.Homiliary, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        for (AbstractCard card : cpr().drawPile.group) {
            if (isCardTypeOf(card, AbstractCard.CardType.ATTACK) && card.color == NesCardEnum.Nemesis)
                card.tags.add(PrefabCardTags.LEECH);
        }
        for (AbstractCard card : cpr().hand.group) {
            if (isCardTypeOf(card, AbstractCard.CardType.ATTACK) && card.color == NesCardEnum.Nemesis)
                card.tags.add(PrefabCardTags.LEECH);
        }
        for (AbstractCard card : cpr().discardPile.group) {
            if (isCardTypeOf(card, AbstractCard.CardType.ATTACK) && card.color == NesCardEnum.Nemesis)
                card.tags.add(PrefabCardTags.LEECH);
        }
    }

    @Override
    public int onPlayerHeal(int healAmount) {
        if (AbstractDungeon.currMapNode != null && currRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            healAmount *= (1 - HealEffective);
        }
        return super.onPlayerHeal(healAmount);
    }
}