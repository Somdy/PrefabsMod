package rs.prefabs.general.utils;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public class PrefabData {
    private Properties properties;
    private List<Object> objects;
    private List<Map<String, Object>> sMaps;
    private List<Map<String, Integer[]>> iMaps;
    
    public PrefabData() {
        properties = new Properties();
        objects = new ArrayList<>();
        sMaps = new ArrayList<>();
        iMaps = new ArrayList<>();
    }
    
    public PrefabData build() {
        if (properties != null && properties.isEmpty()) properties = null;
        if (objects != null && objects.isEmpty()) objects = null;
        if (sMaps != null && sMaps.isEmpty()) sMaps = null;
        if (iMaps != null && iMaps.isEmpty()) iMaps = null;
        return this;
    }
    
    public PrefabData reset() {
        properties = new Properties();
        objects = new ArrayList<>();
        sMaps = new ArrayList<>();
        iMaps = new ArrayList<>();
        return this;
    }
    
    public PrefabData copyFrom(@NotNull PrefabData data) {
        reset();
        properties = data.properties;
        for (Object v : data.objects) {
            if (v != null)
                addNewValue(v);
        }
        for (Map<String, Object> item : data.sMaps) {
            if (!item.isEmpty())
                createNewSMap(item);
        }
        for (Map<String, Integer[]> item : data.iMaps) {
            if (!item.isEmpty())
                createNewIMap(item);
        }
        return this;
    }

    public boolean isValid() {
        return (properties != null && objects != null && sMaps != null)
                && (!properties.isEmpty() || !objects.isEmpty() || !sMaps.isEmpty());
    }
    
    public boolean setNewProperty(String key, String value) {
        return properties.setProperty(key, value) == null;
    }
    
    public Properties properties() {
        return properties;
    }

    public boolean addNewValue(Object value) {
        return addNewValue(value, false);
    }
    
    public boolean addNewValue(Object value, boolean repeatable) {
        if (!objects.contains(value) || repeatable)
            return objects.add(value);
        return false;
    }
    
    public List<Object> values() {
        return objects;
    }

    public boolean createNewSMap(Map<String, Object> item) {
        return createNewSMap(item, false);
    }
    
    public boolean createNewSMap(Map<String, Object> item, boolean repeatable) {
        if (!sMaps.contains(item) || repeatable)
            return sMaps.add(item);
        return false;
    }

    public Map<String, Object> sMap() {
        return sMap(m ->true);
    }
    
    public Map<String, Object> sMap(Predicate<Map<String, Object>> expt) {
        return sMaps.stream().filter(expt).findFirst().orElse(null);
    }

    public boolean createNewIMap(Map<String, Integer[]> item) {
        return createNewIMap(item, false);
    }

    public boolean createNewIMap(Map<String, Integer[]> item, boolean repeatable) {
        if (!iMaps.contains(item) || repeatable)
            return iMaps.add(item);
        return false;
    }

    public Map<String, Integer[]> iMap() {
        return iMap(m -> true);
    }

    public Map<String, Integer[]> iMap(Predicate<Map<String, Integer[]>> expt) {
        return iMaps.stream().filter(expt).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "PrefabData{" +
                (properties != null ? "properties=" + properties.toString() : "") +
                (objects != null ? ", objects=" + objects.toString() : "") +
                (sMaps != null ? ", sMaps=" + sMaps.toString() : "") +
                (iMaps != null ? ", iMaps=" + iMaps.toString() : "") +
                '}';
    }
}