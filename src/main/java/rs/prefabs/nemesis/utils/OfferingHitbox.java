package rs.prefabs.nemesis.utils;

import com.megacrit.cardcrawl.helpers.Hitbox;

public class OfferingHitbox extends Hitbox {
    boolean occupied;
    
    public OfferingHitbox(float width, float height) {
        super(width, height);
        occupied = false;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isOccupied() {
        return occupied;
    }
}