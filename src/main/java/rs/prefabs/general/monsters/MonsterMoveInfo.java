package rs.prefabs.general.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

public class MonsterMoveInfo extends EnemyMoveInfo {
    public String moveName;
    public DamageInfo info;
    public AbstractCreature target;

    public MonsterMoveInfo(String moveName, byte nextMove, AbstractMonster.Intent intent, DamageInfo info, AbstractCreature target, int multiplier) {
        super(nextMove, intent, info != null ? info.base : 0, multiplier, multiplier > 1);
        this.moveName = moveName;
        this.target = target;
    }
    
    public MonsterMoveInfo(byte nextMove, AbstractMonster.Intent intent, DamageInfo info, AbstractCreature target, int multiplier) {
        this(null, nextMove, intent, info, target, multiplier);
    }

    public MonsterMoveInfo(String moveName, byte nextMove, AbstractMonster.Intent intent, AbstractCreature target) {
        this(moveName, nextMove, intent, null, target, 0);
    }

    public MonsterMoveInfo(byte nextMove, AbstractMonster.Intent intent, AbstractCreature target) {
        this(null, nextMove, intent, null, target, 0);
    }
}