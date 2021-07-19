package rs.prefabs.general.misc;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.cards.DamageSource;

public class PrefabDmgInfo extends DamageInfo {
    public DamageSource source;
    
    public PrefabDmgInfo(@NotNull DamageSource source, int base, DamageType type) {
        super(source.getSource(), base, type);
        this.source = source;
    }
    
    @NotNull
    public static PrefabDmgInfo[] createInfoArray(PrefabDmgInfo baseInfo) {
        return createInfoArray(baseInfo, false);
    }
    
    @NotNull
    public static PrefabDmgInfo[] createInfoArray(PrefabDmgInfo baseInfo, boolean pureDmg) {
        PrefabDmgInfo[] infos = new PrefabDmgInfo[AbstractDungeon.getMonsters().monsters.size()];
        for (int i = 0; i < infos.length; i++) {
            PrefabDmgInfo tmp = new PrefabDmgInfo(baseInfo.source, baseInfo.base, baseInfo.type);
            if (!pureDmg)
                tmp.applyPowers(baseInfo.owner, AbstractDungeon.getMonsters().monsters.get(i));
            infos[i] = tmp;
        }
        return infos;
    }
}