package rs.prefabs.general.misc;

import java.util.Objects;

public class PrefabClass<T> {
    private final T family;
    private Object species;
    
    public PrefabClass(T family) {
        this.family = family;
    }
    
    public PrefabClass<T> create(Object species) {
        this.species = species;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrefabClass)) return false;
        PrefabClass<?> that = (PrefabClass<?>) o;
        boolean familyEqual = Objects.equals(family, that.family);
        boolean speciesEqual = species == null || species.equals(that.species);
        return familyEqual && speciesEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(family, species);
    }
}