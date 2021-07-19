package rs.prefabs.general.abstracts;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import rs.prefabs.general.misc.PrefabClass;
import rs.prefabs.general.utils.GeneralUtils;

import java.util.Optional;

public abstract class AbstractPrefabRelic extends CustomRelic implements GeneralUtils {
    private boolean rightClickStarted;
    private boolean rightClicked;
    
    protected PrefabClass<AbstractPrefabRelic> clazz;
    
    public AbstractPrefabRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
        super(id, texture, outline, tier, sfx);
        rightClicked = rightClickStarted = false;
        clazz = new PrefabClass<>(this);
    }

    public PrefabClass<AbstractPrefabRelic> getClazz() {
        return clazz;
    }

    protected void setPrefabType(PrefabClass<AbstractPrefabRelic> clazz) {
        this.clazz = clazz;
    }

    protected void addTips(String head, String body) {
        if (tips.stream().noneMatch(t -> t.header.equals(head) || t.body.equals(body)))
            tips.add(new PowerTip(head, body));
    }

    protected void replaceTips(String head, String body) {
        Optional<PowerTip> tip = tips.stream().filter(t -> t.header.equals(head) || t.body.equals(body)).findFirst();
        if (tip.isPresent()) {
            tips.remove(tip.get());
        } else {
            addTips(head, body);
        }
    }
    
    protected void resetOverlayInfo() {
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }
    
    protected AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }
    
    protected boolean onRightClick() {
        return false;
    }

    @Override
    public void update() {
        super.update();
        if (rightClickStarted && InputHelper.justReleasedClickRight) {
            if (hb.hovered)
                rightClicked = true;
            rightClickStarted = false;
        }
        if (rightClicked) {
            onRightClick();
            rightClicked = false;
        }
    }
}