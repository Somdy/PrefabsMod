package rs.prefabs.nemesis.data;

import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.data.CardData;

public class NesCardData extends CardData {
    
    public NesCardData(String id) {
        super(id);
    }

    @Override
    protected void addCustomData(@NotNull Element customs, SData sData) {
        for (Element e : customs.elements()) {
            String key = e.getName().toLowerCase().trim();
            if (key.equals("exmagicnum")) {
                if (e.attribute("base") != null)
                    sData.putCustom("baseexmagicnum", e.attributeValue("base"));
                if (e.attribute("prmt") != null)
                    sData.putCustom("prmtexmagicnum", e.attributeValue("prmt"));
            }
            if (key.equals("withernum")) {
                if (e.attribute("base") != null)
                    sData.putCustom("basewithernum", e.attributeValue("base"));
                if (e.attribute("prmt") != null)
                    sData.putCustom("prmtwithernum", e.attributeValue("prmt"));
            }
            if (key.equals("frantic")) {
                sData.putCustom("frantic", e.getStringValue());
                sData.putCustom("prmtfrantic", e.getStringValue());
            }
            if (key.equals("prmtfrantic")) {
                sData.putCustom("prmtfrantic", e.getStringValue(), true);
            }
            if (key.equals("haunted")) {
                sData.putCustom("haunted", e.getStringValue());
                sData.putCustom("prmtHaunted", e.getStringValue());
            }
            if (key.equals("prmthaunted")) {
                sData.putCustom("prmthaunted", e.getStringValue(), true);
            }
        }
    }
}
