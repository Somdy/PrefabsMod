package rs.prefabs.nemesis.interfaces;

public interface AbstractOffering {
    void triggerOnInitializationAndCompletion();
    default void onOfferInitialized() {}
    default void onOfferCompleted() {}
}