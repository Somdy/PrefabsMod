package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.graphics.Color;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.nemesis.cards.AbstractNesCard;
import rs.prefabs.nemesis.data.NesDataReader;

public abstract class AbstractNesGeneralCard extends AbstractNesCard {
    public AbstractNesGeneralCard() {
        super(NesDataReader.getGeneralCardData());
    }

    @Override
    public boolean canEnemyUse() {
        return false;
    }

    @NotNull
    @Override
    public Color getFavoriteColor() {
        return quickColor(232, 71, 15);
    }

    @Override
    protected String getKey() {
        return "general";
    }
}