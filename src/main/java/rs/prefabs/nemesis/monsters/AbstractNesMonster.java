package rs.prefabs.nemesis.monsters;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import rs.prefabs.general.abstracts.AbstractPrefabMonster;
import rs.prefabs.general.misc.SubPrefabClass;

public abstract class AbstractNesMonster extends AbstractPrefabMonster {
    protected final MonsterStrings monsterStrings;
    protected final String NAME;
    protected final String[] DIALOG;
    protected final String[] MOVES;
    
    public AbstractNesMonster(String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, float x, float y) {
        super(id, maxHealth, hb_x, hb_y, hb_w, hb_h, x, y);
        
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(id);
        NAME = monsterStrings.NAME;
        DIALOG = monsterStrings.DIALOG;
        MOVES = monsterStrings.MOVES;
        
        this.name = NAME;
        setPrefabType(clazz.create(new SubPrefabClass<AbstractNesMonster>().create(getKey())));
    }
    
    protected String getKey() {
        return "NES_Monster";
    }
}