package rs.prefabs.nemesis.cards.temps;

import rs.prefabs.nemesis.cards.AbstractNesCard;
import rs.prefabs.nemesis.data.NesDataReader;

public abstract class AbstractNesTempCard extends AbstractNesCard {
    public AbstractNesTempCard() {
        super(NesDataReader.getTempCardData());
    }

    @Override
    public boolean canEnemyUse() {
        return false;
    }

    @Override
    protected String getKey() {
        return "temp";
    }
}