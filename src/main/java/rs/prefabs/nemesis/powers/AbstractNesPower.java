package rs.prefabs.nemesis.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.prefabs.general.abstracts.AbstractPrefabPower;
import rs.prefabs.general.utils.PrefabImgMst;
import rs.prefabs.nemesis.NesDebug;
import rs.prefabs.nemesis.utils.NesGeneralUtils;

import java.util.function.Consumer;

public abstract class AbstractNesPower extends AbstractPrefabPower implements NesGeneralUtils {
    
    protected void Log(Object what) {
        NesDebug.Log(this, what);
    }
    
    @Override
    protected TextureAtlas getPowerAtlas() {
        return PrefabImgMst.getNesPowerAtlas();
    }
    
    public void beforeCardPutOnOffer(AbstractCard card, int turns, Consumer<AbstractCard> sideEffect) {}
    public void afterCardPutOnOffer(AbstractCard card, int turns) {}
}