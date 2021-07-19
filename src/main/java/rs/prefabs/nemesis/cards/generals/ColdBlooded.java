package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.cards.PrefabCardTags;
import rs.prefabs.nemesis.cards.AbstractNesCard;
import rs.prefabs.nemesis.character.NCP;
import rs.prefabs.nemesis.powers.ColdBloodedPower;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ColdBlooded extends AbstractNesGeneralCard {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (canTriggerEnchantedEffect()) {
            addToBot(new QuickAction().setAction(() -> {
                List<AbstractCard> tmp = new ArrayList<>();
                for (int i = 0; i < ExMagicNum; i++) {
                    Optional<AbstractNesCard> card = getExptRandomCard(cardRandomRng(), c -> c.hasTag(PrefabCardTags.LEECH), 
                            NCP.GetAllCards());
                    card.ifPresent(tmp::add);
                }
                if (!tmp.isEmpty()) {
                    tmp.forEach(c -> addToTop(new MakeTempCardInHandAction(c, true, true)));
                }
            }));
        }
        addToBot(new ApplyPowerAction(s, s, new ColdBloodedPower(magicNumber, upgraded ? 0 : 2)));
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }
}