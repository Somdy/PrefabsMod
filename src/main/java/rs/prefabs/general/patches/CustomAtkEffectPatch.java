package rs.prefabs.general.patches;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rs.prefabs.general.utils.AtkEffectMgr;

import java.lang.reflect.Field;

public class CustomAtkEffectPatch {
    @SpirePatch(clz = FlashAtkImgEffect.class, method = "loadImage")
    public static class LoadImagePatch {
        @SpirePrefixPatch
        public static SpireReturn<TextureAtlas.AtlasRegion> Prefix(FlashAtkImgEffect _inst) throws Exception {
            Field efct = _inst.getClass().getDeclaredField("effect");
            efct.setAccessible(true);
            AbstractGameAction.AttackEffect effect = (AbstractGameAction.AttackEffect) efct.get(_inst);
            if (AtkEffectMgr.contains(effect)) {
                return SpireReturn.Return(AtkEffectMgr.getImg(effect));
            }
            return SpireReturn.Continue();
        }
    }
    
    @SpirePatch(clz = FlashAtkImgEffect.class, method = "playSound")
    public static class PlaySoundPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(FlashAtkImgEffect _inst, AbstractGameAction.AttackEffect effect) {
            if (AtkEffectMgr.contains(effect)) {
                CardCrawlGame.sound.play(AtkEffectMgr.getSound(effect));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}