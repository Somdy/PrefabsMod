package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.actions.utility.QuickAction;
import rs.prefabs.nemesis.character.OfferHelper;
import rs.prefabs.nemesis.monsters.Voidremnant;

import java.util.*;

public class ExileToTheVoid extends AbstractNesGeneralCard {
    private int exiles;
    private boolean exiled;
    
    public ExileToTheVoid() {
        super();
        exiles = 0;
        exiled = false;
        cardsToPreview = new ExileToTheVoid(true);
        if (!OfferHelper.unofferableCards.contains(this))
            OfferHelper.unofferableCards.add(this);
        addTip(MSG[0], MSG[1]);
    }
    
    public ExileToTheVoid(boolean exiled) {
        super();
        exiles = 0;
        this.exiled = exiled;
        if (exiled) becomeVoid();
    }
    
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (!exiled && s.maxHealth > 1 && t instanceof AbstractMonster && ((AbstractMonster) t).type != AbstractMonster.EnemyType.BOSS) {
            addToBot(new QuickAction().setAction(() -> {
                addToTop(new InstantKillAction(t));
                addToBot(new DelayAction(this::becomeVoid));
                addToBot(new DelayAction(() -> s.decreaseMaxHealth(MathUtils.ceil(s.maxHealth * 0.2F))));
            }));
        }
    }

    @Override
    protected void spectralize() {
        appendDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    protected void despectralize() {
        subtractDescription(EXTENDED_DESCRIPTION[0]);
    }

    @Override
    public boolean canEnchant() {
        return super.canEnchant() && !exiled;
    }

    @Override
    public void triggerWhenDrawn() {
        if (exiled && hasAnyExptCard(c -> c != this, cpr().hand.group)) {
            addToBot(new QuickAction().setAction(() -> {
                Optional<AbstractCard> card = getExptRandomCard(cardRandomRng(), c -> c != this, cpr().hand.group);
                card.ifPresent(c -> addToTop(new ExhaustSpecificCardAction(c, cpr().hand)));
            }));
        }
    }

    @Override
    public void triggerOnExhaust() {
        if (exiled && exiles > 0) {
            addToBot(new QuickAction().setAction(() -> {
                int count = 0;
                int i = 1;
                while (count < exiles) {
                    int first = MathUtils.ceil((i / (exiles * 1F)) * 10F + currFloor() / (exiles * 1F)) * currAct();
                    int second = MathUtils.ceil((i / (exiles * 1F)) * 20F + currFloor() / (exiles * 1F)) * currAct();
                    if (first <= 0) first = 20;
                    if (second <= 0) second = 40;
                    addToBot(new SpawnMonsterAction(new Voidremnant(Math.min(first, second), Math.max(first, second),
                            scale(100F) - i * 150F, MathUtils.sin(i) * 100 + scale(260F)), false));
                    i++;
                }
            }));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && !exiled && p.maxHealth > 1;
    }

    public ExileToTheVoid becomeVoid() {
        upgradeBaseCost(-2);
        upgradeName(MSG[2]);
        upgradeDescription(MSG[3]);
        removeTip(MSG[0], MSG[1]);
        cardsToPreview = null;
        exiled = true;
        if (!OfferHelper.unofferableCards.contains(this))
            OfferHelper.unofferableCards.add(this);
        return this;
    }

    @Override
    protected Map<String, String> createCustomMapOnSaving() {
        Map<String, String> map = new HashMap<>();
        map.put("Voidres", String.valueOf(exiles));
        return map;
    }

    @Override
    protected void receiveCustomMapOnLoading(Map<String, String> map) {
        if (map == null) {
            Log("Unable to find the voidre pairs");
            return;
        }
        if (map.containsKey("Voidres"))
            exiles = Integer.parseInt(map.get("Voidres"));
    }
}