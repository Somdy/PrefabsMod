package rs.prefabs.general.interfaces;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface MonsterEndTurnSubscriber {
    void receiveOnMonsterTurnEnds(AbstractMonster m);
}