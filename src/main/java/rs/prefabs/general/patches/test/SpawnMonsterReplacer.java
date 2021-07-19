package rs.prefabs.general.patches.test;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import rs.prefabs.nemesis.monsters.Voidremnant;

public class SpawnMonsterReplacer {
    @SpirePatch(clz = MonsterHelper.class, method = "getEncounter")
    public static class MonsterGroupReplacer {
        /*@SpirePrefixPatch
        public static SpireReturn<MonsterGroup> returnCustomizedGroups(String key) {
            AbstractMonster[] monsters = new AbstractMonster[] {
                    new Voidremnant(60, 70, 0, 0)
            };
            return SpireReturn.Return(new MonsterGroup(monsters));
        }*/
    }
}