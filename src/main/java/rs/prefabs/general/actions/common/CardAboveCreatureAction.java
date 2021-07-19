package rs.prefabs.general.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.vfx.combat.CardAboveCreatureEffect;

public class CardAboveCreatureAction extends AbstractPrefabGameAction {
    private AbstractCreature target;
    private AbstractCard card;
    private boolean showed;

    public CardAboveCreatureAction(AbstractCreature target, AbstractCard card) {
        this.target = target;
        this.card = card;
        showed = false;
        actionType = AbstractGameAction.ActionType.TEXT;
        duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        if (!showed) {
            AbstractDungeon.effectList.add(new CardAboveCreatureEffect(target, card));
            showed = true;
        }
        tickDuration();
    }
}