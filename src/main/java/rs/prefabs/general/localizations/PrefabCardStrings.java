package rs.prefabs.general.localizations;

import org.jetbrains.annotations.NotNull;

public class PrefabCardStrings {
    public String FAKENAME;
    public String TRUENAME;
    public String DESCRIPTION;
    public String UPGRADE_DESCRIPTION;
    public String[] EXTENDED_DESCRIPTION;
    public String[] MSG;

    @NotNull
    public static PrefabCardStrings getMockingStrings() {
        PrefabCardStrings retVal = new PrefabCardStrings();
        retVal.FAKENAME = "[MISSING_TITLE]";
        retVal.TRUENAME = "[MISSING_FLAVOR]";
        retVal.DESCRIPTION = "[MISSING_DESCRIPTION]";
        retVal.UPGRADE_DESCRIPTION = "[MISSING_DESCRIPTION+]";
        retVal.EXTENDED_DESCRIPTION = new String[] {"[MISSING_0]", "[MISSING_1]", "[MISSING_2]"};
        retVal.MSG = new String[] {"[MISSING_0]", "[MISSING_1]", "[MISSING_2]"};
        return retVal;
    }
}