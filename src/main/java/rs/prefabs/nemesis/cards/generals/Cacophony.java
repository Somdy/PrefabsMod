package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.common.DamageAndDoWhenUnblocked;
import rs.prefabs.general.actions.utility.QuickAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cacophony extends AbstractNesGeneralCard {

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction().setAction(() -> {
            List<AbstractCreature> targets = upgraded ? getAllLivingCreatures()
                    : getAllExptCreatures(c -> c != null && !c.isDeadOrEscaped() && c.currentBlock <= 0);
            for (int i = 0; i < magicNumber; i++) {
                for (AbstractCreature c : targets) {
                    addToBot(new DamageAndDoWhenUnblocked(c, crtDmgInfo(s, damage, damageTypeForTurn), 
                            AbstractGameAction.AttackEffect.BLUNT_LIGHT, crt -> {
                        if (!cpr().hand.isEmpty()) {
                            List<AbstractCard> tmp = new ArrayList<>(cpr().hand.group);
                            tmp.removeIf(card -> !isCardEnchantable(card));
                            if (!tmp.isEmpty()) {
                                for (int k = 0; k < ExMagicNum; k++) {
                                    Optional<AbstractCard> card = getRandomCard(tmp, cardRandomRng());
                                    card.ifPresent(ca -> {
                                        enchantCard(ca);
                                        tmp.remove(ca);
                                    });
                                }
                            }
                        }
                    }));
                }
            }
        }));
    }

    @Override
    protected void spectralize() {
        enchantDamage(0, baseDamage + 4);
    }

    @Override
    protected void despectralize() {
        disenchantDamage(0);
    }
}