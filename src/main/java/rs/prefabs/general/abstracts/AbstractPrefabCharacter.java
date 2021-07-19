package rs.prefabs.general.abstracts;

import basemod.abstracts.CustomPlayer;
import rs.prefabs.general.utils.GeneralUtils;

public abstract class AbstractPrefabCharacter extends CustomPlayer implements GeneralUtils {
    public AbstractPrefabCharacter(String name, PlayerClass playerClass) {
        super(name, playerClass, null, null, (String) null, null);
    }
}