package rs.prefabs.nemesis.actions.common;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.nemesis.NesDebug;
import rs.prefabs.nemesis.interfaces.FranticItem;

public class FranticCardAutoplayAction extends AbstractPrefabGameAction {
    private AbstractCard card;
    private CardGroup container;
    
    public FranticCardAutoplayAction(AbstractCard card, CardGroup container) {
        this.card = card;
        this.container = container;
    }
    
    @Override
    public void update() {
        isDone = true;
        if (container.contains(card)) {
            AbstractMonster t = AbstractDungeon.getRandomMonster();
            int enengyOnUse = MathUtils.ceil(getCardRealCost(card) / 2F);
            card.targetAngle = 0F;
            card.current_x = cpr().drawX;
            card.current_y = cpr().hb.cY;
            card.target_x = t.drawX;
            card.target_y = t.hb.cY;
            card.calculateCardDamage(t);
            NesDebug.Log(this, "Autoplaying frantic card " + card.name + " at cost of " + enengyOnUse);
            CardQueueItem item = new CardQueueItem(card, t, enengyOnUse, false, true);
            AbstractDungeon.actionManager.addCardQueueItem(item, true);
            AbstractDungeon.player.loseEnergy(enengyOnUse); // STUPID THING THAT AUTOPLAY MUST COST 0 BY DEFAULT!!!
            if (card instanceof FranticItem) ((FranticItem) card).triggerOnFranticPlay(item.monster);
        }
    }
}