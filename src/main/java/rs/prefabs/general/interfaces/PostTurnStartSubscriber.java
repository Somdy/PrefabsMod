package rs.prefabs.general.interfaces;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface PostTurnStartSubscriber {
    void receivePostTurnStart(AbstractCreature creature, boolean postDraw);
}