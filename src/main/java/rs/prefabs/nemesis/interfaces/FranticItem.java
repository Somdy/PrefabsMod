package rs.prefabs.nemesis.interfaces;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface FranticItem {
    float factor();
    default void triggerOnFranticPlay(AbstractMonster t) {}
}