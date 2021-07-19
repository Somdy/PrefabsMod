package rs.prefabs.nemesis.utils;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.utils.PrefabUtils;
import rs.prefabs.nemesis.NesDebug;
import rs.prefabs.nemesis.interfaces.FranticItem;

public class FranticActuary {
    private static final double baseDominance = 0.15F;
    private static double dominance = 0.1F;
    
    public static boolean canAutoPlay(@NotNull FranticItem item) {
        float factor = item.factor();
        boolean success;
        if (!cpr().isDeadOrEscaped()) {
            double handsize = cpr().hand.size();
            double gamesize = cpr().gameHandSize;
            double drawsie = cpr().drawPile.size();
            double discardsize = Math.max(cpr().discardPile.size(), 1);
            double mastersize = cpr().masterDeck.size();
            double handmax = BaseMod.MAX_HAND_SIZE;
            double cprhp = cpr().currentHealth;
            double cprmaxhp = cpr().maxHealth;
            double cardsplayed = Math.max(AbstractDungeon.actionManager.cardsPlayedThisCombat.size(), 1);
            double monsters = (int) AbstractDungeon.getMonsters().monsters.stream()
                    .filter(m -> !m.isDeadOrEscaped())
                    .mapToDouble(m -> m.currentHealth)
                    .sum();
            double energy = EnergyPanel.getCurrentEnergy();
            double maxenergy = cpr().energy.energyMaster;
            double handFactor = handsize / handmax;
            double drawFactor = gamesize / handsize;
            double discardFactor = (handsize + gamesize) / discardsize;
            double monsterFactor = cprhp / monsters;
            double masterFactor = mastersize / (handsize + drawsie + discardsize);
            double hpDomin = cprhp / cprmaxhp;
            double cardFactor = mastersize / cardsplayed;
            double energyFactor = maxenergy / energy;
            NesDebug.Log(FranticActuary.class, handFactor + ", " + drawFactor + ", " + discardFactor
                    + ", " + masterFactor + ", " + hpDomin + ", " + cardFactor + ", " + monsterFactor + ", " + energyFactor);
            factor = (float) (dominance * (factor + handFactor * 1.108F + drawFactor * 1.017F + discardFactor * 0.237F
                    + masterFactor * 1.119F - hpDomin * 1.567F - cardFactor * 0.0279F - monsterFactor * 0.115F));
            factor -= energyFactor * 0.15F;
            while (factor > 1F) {
                double residue = Math.floor(factor);
                factor = (float) PrefabUtils.SciRound(factor / residue, 10);
                if (factor <= 2F)
                    factor /= 2F;
            }
            if (factor < 0F) factor = 0F;
        }
        success = AbstractDungeon.cardRandomRng.randomBoolean(factor);
        NesDebug.Log(FranticActuary.class, "Succeeded in autoplaying at chance of " + factor + "? " + success);
        if (!success) dominance += 0.02F;
        else dominance = baseDominance;
        return success;
    }
    
    private static AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }
}