package rs.prefabs.nemesis.actions.unique;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import rs.prefabs.general.abstracts.AbstractPrefabGameAction;
import rs.prefabs.general.actions.common.SimpleHandCardSelectBuilder;
import rs.prefabs.general.actions.utility.DelayAction;
import rs.prefabs.general.tools.HandCardManipulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class RustyObelishAction extends AbstractPrefabGameAction {
    private List<UUID> uuids;
    private AbstractCard.CardColor color;
    private boolean alter;
    private String msg;
    private Consumer<AbstractCard> callback;
    private List<AbstractCard> cards;
    
    public RustyObelishAction(int amount, AbstractCard.CardColor color, boolean alter, String msg, Consumer<AbstractCard> callback) {
        this.amount = amount;
        this.color = color;
        this.alter = alter;
        this.msg = msg;
        this.callback = callback;
        uuids = new ArrayList<>();
        actionType = ActionType.CARD_MANIPULATION;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            cards = new ArrayList<>();
            List<AbstractCard> lib = new ArrayList<>(CardLibrary.getAllCards());
            lib.removeIf(c -> c.color != color);
            if (lib.isEmpty() || amount <= 0) {
                isDone = true;
                return;
            }
            if (amount > lib.size()) amount = lib.size();
            while (cards.size() < amount) {
                Optional<AbstractCard> card = getRandomCard(lib, cardRandomRng());
                if (!card.isPresent()) continue;
                cards.add(card.get());
            }
            cards.forEach(c -> callback.accept(c));
            cards.forEach(c -> {
                if (!uuids.contains(c.uuid))
                    uuids.add(c.uuid);
            });
            cards.forEach(c -> addToTop(new MakeTempCardInHandAction(c, false, true)));
        }
        tickDuration();
        if (isDone && !uuids.isEmpty() && alter) {
            addToTop(new SimpleHandCardSelectBuilder(c -> uuids.stream().anyMatch(u -> u == c.uuid))
                    .setAmount(1)
                    .setCanPickZero(true)
                    .setShouldMatchAll(true)
                    .setMsg(msg)
                    .setManipulator(new HandCardManipulator() {
                        @Override
                        public boolean manipulate(AbstractCard card, int indexOfCard) {
                            if (card != null)
                                addToBottom(new DelayAction(() -> effectToList(new ShowCardAndObtainEffect(card.makeCopy(),
                                        Settings.WIDTH / 2F, Settings.HEIGHT / 2F))));
                            return true;
                        }
                    }));
        }
    }
}