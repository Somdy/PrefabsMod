package rs.prefabs.nemesis.cards.generals;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.general.cards.PrefabCardTags;
import rs.prefabs.nemesis.IDs.CardID;
import rs.prefabs.nemesis.character.NCP;
import rs.prefabs.nemesis.interfaces.TotemOffering;

import java.util.Optional;

public class BloodyBlades extends AbstractNesGeneralCard implements TotemOffering {
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new QuickAction(AbstractGameAction.ActionType.CARD_MANIPULATION)
                .setAction(() -> {
                    for (int i = 0; i < magicNumber; i++) {
                        Optional<AbstractCard> card = NCP.GetCard(CardID.FlashStrike);
                        card.ifPresent(c -> {
                            c.isEthereal = true;
                            c.purgeOnUse = true;
                            c.tags.add(PrefabCardTags.LEECH);
                            if (upgraded) c.upgrade();
                            addToTop(new MakeTempCardInHandAction(c, 1));
                        });
                    }
                }));
    }

    @Override
    protected void spectralize() {
        enchantExMagics(0, baseExMagicNum + 1);
        enchantMagics(1, baseMagicNumber + 1);
    }

    @Override
    protected void despectralize() {
        disenchantExMagics(0);
        disenchantMagics(1);
    }

    @Override
    public void triggerOnInitializationAndCompletion() {
        addToBot(new QuickAction(AbstractGameAction.ActionType.CARD_MANIPULATION)
                .setAction(() -> {
                    for (int i = 0; i < magicNumber; i++) {
                        Optional<AbstractCard> card = NCP.GetCard(CardID.FlashStrike);
                        card.ifPresent(c -> {
                            c.isEthereal = true;
                            c.purgeOnUse = true;
                            c.tags.add(PrefabCardTags.LEECH);
                            if (upgraded) c.upgrade();
                            addToTop(new MakeTempCardInHandAction(c, 1));
                        });
                    }
                }));
    }

    @Override
    public void postCardUsed(AbstractCard card, UseCardAction action) {
        if (upgraded && !card.isInAutoplay && isCardTypeOf(card, CardType.ATTACK)) {
            flash();
            for (int i = 0; i < ExMagicNum; i++) {
                Optional<AbstractMonster> m = getRandom(getAllLivingMstrs(), cardRandomRng());
                m.ifPresent(mo -> {
                    Optional<AbstractCard> c = NCP.GetCard(CardID.FlashStrike);
                    c.ifPresent(ca -> {
                        ca.damage = ca.baseDamage /= 2;
                        ca.isDamageModified = true;
                        ca.purgeOnUse = true;
                        addToTop(new NewQueueCardAction(ca, mo, true, true));
                    });
                });
            }
        }
    }

    @Override
    public void atStartOfTurn(AbstractCreature who, boolean postDraw) {
        if (!upgraded && who == cpr() && !postDraw) {
            addToBot(new QuickAction().setAction(() -> {
                Optional<AbstractMonster> m = getRandom(getAllLivingMstrs(), cardRandomRng());
                m.ifPresent(mo -> {
                    for (int i = 0; i < ExMagicNum; i++) {
                        Optional<AbstractCard> card = NCP.GetCard(CardID.FlashStrike);
                        card.ifPresent(c -> {
                            c.tags.add(PrefabCardTags.LEECH);
                            c.purgeOnUse = true;
                            addToTop(new NewQueueCardAction(c, mo, true, true));
                        });
                    }
                });
            }));
        }
    }
}