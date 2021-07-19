package rs.prefabs.general.misc;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SubPrefabClass<E> {
    private E value;
    private Object sub;
    
    public SubPrefabClass(@NotNull E value) {
        this.value = value;
        sub = null;
    }

    public SubPrefabClass() {
        
    }
    
    public SubPrefabClass<E> create(Object sub) {
        this.sub = sub;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubPrefabClass)) return false;
        SubPrefabClass<?> that = (SubPrefabClass<?>) o;
        return value.equals(that.value) && Objects.equals(sub, that.sub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, sub);
    }
}