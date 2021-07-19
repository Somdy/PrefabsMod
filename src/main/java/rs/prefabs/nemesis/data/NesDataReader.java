package rs.prefabs.nemesis.data;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jetbrains.annotations.Contract;
import rs.prefabs.general.data.CardData;
import rs.prefabs.nemesis.NesDebug;

import java.net.URL;

public class NesDataReader {
    private static CardData generals;
    private static CardData temps;
    
    public static void initialize() {
        SAXReader reader = new SAXReader();
        initGeneralCardData(reader);
        initTempCardData(reader);
    }
    
    private static void initGeneralCardData(SAXReader reader) {
        try {
            Document data = reader.read(loadFileFromString("PrefabsAssets/NemesisProperties/data/cards/general.xml"));
            Element root = data.getRootElement();
            Element cardata = root.element("CardData");
            generals = new NesCardData("general").copyData(cardata);
        } catch (Exception e) {
            Log("Failed to load general card data from XML");
            e.printStackTrace();
        }
    }
    
    private static void initTempCardData(SAXReader reader) {
        try {
            Document data = reader.read(loadFileFromString("PrefabsAssets/NemesisProperties/data/cards/temp.xml"));
            Element root = data.getRootElement();
            Element cardata = root.element("CardData");
            temps = new NesCardData("temp").copyData(cardata);
        } catch (Exception e) {
            Log("Failed to load temp card data from XML");
            e.printStackTrace();
        }
    }
    
    public static CardData getGeneralCardData() {
        return generals;
    }

    public static CardData getTempCardData() {
        return temps;
    }

    @Contract("_ -> new")
    private static URL loadFileFromString(String path) {
        return NesDataReader.class.getClassLoader().getResource(path);
    }
    
    private static void Log(Object what) {
        NesDebug.Log(NesDataReader.class, what);
    }
}