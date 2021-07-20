package rs.prefabs.nemesis.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.BobEffect;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.PrefabMgr;
import rs.prefabs.nemesis.NesDebug;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.interfaces.*;
import rs.prefabs.nemesis.powers.AbstractNesPower;
import rs.prefabs.nemesis.utils.OfferingHitbox;

import java.util.*;
import java.util.function.Consumer;

public class OfferHelper {
    private static final float offerScale;
    private static final float offerHoveredScale;
    private static final float HB_OFFSETX;
    private static final float HB_OFFSETY;
    private static final float STARTX_OFFSET;
    private static final float CARDX_OFFSET;
    private static final float CARDY_OFFSET;
    private static final Texture emptySlot = ImageMaster.loadImage("PrefabsAssets/NemesisProperties/images/ui/oblation/emptyBody.png");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(NesFab.makeID("OfferHelper"));
    private static final String[] TEXT = uiStrings.TEXT;
    private static boolean hoveringCard;
    private static float SLOT_HEIGHT;
    private static boolean valid;
    private static CardGroup offers;
    public static int maxSlot;
    public static Vector2[] slots;
    public static OfferingHitbox[] hbs;
    public static BobEffect[] bobs;
    public static Map<AbstractCard, Integer> OfferLogics;
    public static List<OfferingSideEffect<AbstractCard, Consumer<AbstractCard>, OfferingSideEffectDescription>> sideEffects;
    public static List<AbstractCard> CardsOfferedThisCombat;
    public static List<AbstractCard> UnofferableCards;
    
    static {
        offerScale = 0.35F;
        offerHoveredScale = 0.65F;
        HB_OFFSETX = 46.5F * Settings.xScale;
        HB_OFFSETY = 50F * Settings.yScale;
        STARTX_OFFSET = 25F * Settings.xScale;
        CARDX_OFFSET = 45F * Settings.xScale;
        CARDY_OFFSET = 56F * Settings.yScale;
        
        hoveringCard = false;
        valid = false;
        SLOT_HEIGHT = 420F * Settings.yScale;
        maxSlot = 4;
        slots = new Vector2[maxSlot];
        hbs = new OfferingHitbox[maxSlot];
        bobs = new BobEffect[maxSlot];
        offers = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        OfferLogics = new HashMap<>();
        sideEffects = new ArrayList<>();
        CardsOfferedThisCombat = new ArrayList<>();
        UnofferableCards = new ArrayList<>();
    }
    
    public static void initPreBattle() {
        hoveringCard = false;
        valid = true;
        SLOT_HEIGHT = cpr().drawY + 420F * Settings.yScale;
        initSlotVectors();
        initSlotHbs();
        Arrays.fill(bobs, new BobEffect(0.5F));
        offers.clear();
        OfferLogics.clear();
        sideEffects.clear();
        CardsOfferedThisCombat.clear();
        UnofferableCards.clear();
    }

    public static void ResizeOfferingBox(int delta) {
        if (delta > 0)
            incrsMaxOfferingBox(delta);
        else if (delta < 0)
            decrsMaxOfferingBox(delta);
    }

    private static void decrsMaxOfferingBox(int decrement) {
        if (maxSlot - decrement <= 0) {
            NesDebug.Log("There must be at least an offering box left");
            return;
        }
        maxSlot -= decrement;
        initSlotVectors();
        initSlotHbs();
        bobs = new BobEffect[maxSlot];
        Arrays.fill(bobs, new BobEffect(0.5F));
    }

    private static void incrsMaxOfferingBox(int increment) {
        maxSlot += increment;
        initSlotVectors();
        initSlotHbs();
        bobs = new BobEffect[maxSlot];
        Arrays.fill(bobs, new BobEffect(0.5F));
    }
    
    public static void clearPostBattle() {
        if (valid) {
            offers.clear();
            OfferLogics.clear();
            sideEffects.clear();
            CardsOfferedThisCombat.clear();
            UnofferableCards.clear();
            valid = false;
        }
    }
    
    private static void initSlotVectors() {
        slots = new Vector2[maxSlot];
        float startX = adjustFirstSlotX();
        for (int i = 0; i < maxSlot; i++) {
            NesDebug.Log("Setting vector " + i + " at " + (startX + 86F * i) + ", " + SLOT_HEIGHT);
            slots[i] = new Vector2(startX + 86F * i, SLOT_HEIGHT);
        }
    }
    
    private static void initSlotHbs() {
        hbs = new OfferingHitbox[maxSlot];
        float startX = adjustFirstSlotX() + HB_OFFSETX;
        for (int i = 0; i < maxSlot; i++) {
            hbs[i] = new OfferingHitbox(86F, 100F);
            NesDebug.Log("Moving hitbox " + i + " to " + (startX + 86F * i) + ", " + (SLOT_HEIGHT + HB_OFFSETY));
            hbs[i].move(startX + 86F * i, SLOT_HEIGHT + HB_OFFSETY);
        }
    }
    
    public static boolean HasEmptySlot() {
        if (offers != null)
            return offers.size() - 1 < maxSlot && Arrays.stream(hbs).anyMatch(hb -> !hb.isOccupied());
        return false;
    }

    public static boolean AddOffering(AbstractCard card, int turnsLeft, Consumer<AbstractCard> sideEffect,
                                      OfferingSideEffectDescription sideEffectDescription) {
        if (!CanOffer()) return false;
        if (!HasEmptySlot()) return false;
        if (UnofferableCards.contains(card)) return false;
        contactPowersBeforeCardPutOnOffer(card, turnsLeft, sideEffect);
        card.beginGlowing();
        card.targetAngle = 0F;
        card.targetDrawScale = offerScale;
        card.target_x = slots[offers.size()].x + CARDX_OFFSET;
        card.target_y = slots[offers.size()].y + CARDY_OFFSET;
        if (card instanceof AbstractOffering) {
            ((AbstractOffering) card).triggerOnInitializationAndCompletion();
            ((AbstractOffering) card).onOfferInitialized();
        }
        hbs[offers.size()].setOccupied(true);
        offers.addToTop(card);
        sideEffects.add(createSideEffect(card, sideEffect, sideEffectDescription));
        addOfferingLogic(card, turnsLeft);
        contactCardsOnAddingOffering(card);
        contactPowersAfterCardPutOnOffer(card, turnsLeft);
        return true;
    }

    public static boolean AddOffering(AbstractCard card, int turnsLeft) {
        return AddOffering(card, turnsLeft, null, null);
    }
    
    public static List<AbstractCard> GetOfferings() {
        return offers.group;
    }

    private static void contactPowersAfterCardPutOnOffer(AbstractCard card, int turnsLeft) {
        for (AbstractPower p : cpr().powers) {
            if (p instanceof AbstractNesPower)
                ((AbstractNesPower) p).afterCardPutOnOffer(card, turnsLeft);
        }
    }
    
    private static void contactPowersBeforeCardPutOnOffer(AbstractCard card, int turnsLeft, Consumer<AbstractCard> sideEffect) {
        for (AbstractPower p : cpr().powers) {
            if (p instanceof AbstractNesPower)
                ((AbstractNesPower) p).beforeCardPutOnOffer(card, turnsLeft, sideEffect);
        }
    }
    
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    private static OfferingSideEffect<AbstractCard, Consumer<AbstractCard>, OfferingSideEffectDescription> 
    createSideEffect(AbstractCard targetCard, Consumer<AbstractCard> sideEffect, OfferingSideEffectDescription description) {
        return new OfferingSideEffect<AbstractCard, Consumer<AbstractCard>, OfferingSideEffectDescription>() {
            @Override
            public Consumer<AbstractCard> accept(AbstractCard card) {
                return sideEffect;
            }

            @Override
            public OfferingSideEffectDescription find(AbstractCard card) {
                return description;
            }

            @Override
            public boolean identify(AbstractCard card) {
                return card == targetCard;
            }
        };
    }
    
    private static void addOfferingLogic(AbstractCard card, int turnsLeft) {
        OfferLogics.put(card, turnsLeft);
    }

    private static void contactCardsOnAddingOffering(AbstractCard offering) {
        for (AbstractCard c : cpr().hand.group) {
            if (c instanceof OfferingContacter)
                ((OfferingContacter) c).onAddingNewOffering(offering);
        }
        for (AbstractCard c : cpr().drawPile.group) {
            if (c instanceof OfferingContacter)
                ((OfferingContacter) c).onAddingNewOffering(offering);
        }
        for (AbstractCard c : cpr().discardPile.group) {
            if (c instanceof OfferingContacter)
                ((OfferingContacter) c).onAddingNewOffering(offering);
        }
        for (AbstractCard c : cpr().exhaustPile.group) {
            if (c instanceof OfferingContacter)
                ((OfferingContacter) c).onAddingNewOffering(offering);
        }
    }
    
    public static void OnCardPlayed(AbstractCard card, UseCardAction action) {
        for (AbstractCard c : offers.group) {
            if (c instanceof TotemOffering)
                ((TotemOffering) c).postCardUsed(card, action);
        }
    }
    
    public static void AtStartOfTurn(AbstractCreature who, boolean postDraw) {
        for (AbstractCard c : offers.group) {
            if (c instanceof TotemOffering)
                ((TotemOffering) c).atStartOfTurn(who, postDraw);
        }
    }
    
    public static void render(SpriteBatch sb) {
        if (!CanOffer()) return;
        for (OfferingHitbox hb : hbs) {
            hb.render(sb);
        }
        sb.setColor(Color.WHITE.cpy());
        for (int i = 0; i < maxSlot; i++) {
            if (offers.size() - 1 >= i && offers.group.get(i) != null)
                offers.group.get(i).render(sb);
            else {
                sb.draw(emptySlot, slots[i].x, slots[i].y, 0F, 0F,
                        emptySlot.getWidth() * Settings.scale, emptySlot.getHeight() * Settings.scale, 1F, 1F,
                        0F, 0, 0, emptySlot.getWidth(), emptySlot.getHeight(), false, false);
            }
        }
    }
    
    public static void update() {
        if (!CanOffer()) return;
        updateHitboxLogic();
        for (BobEffect bob : bobs)
            bob.update();
        for (int i = 0; i < offers.size(); i++) {
            AbstractCard card = offers.group.get(i);
            card.target_y = slots[i].y + bobs[i].y + CARDY_OFFSET;
            card.update();
            card.updateHoverLogic();
            updateCardHoverLogic(card);
            if (!OfferLogics.containsKey(card)) {
                NesDebug.Log(card.name + " has no offering logics, would be set to 1.");
                addOfferingLogic(card, 1);
            }
        }
    }
    
    private static void updateCardHoverLogic(@NotNull AbstractCard card) {
        card.hb.update();
        if (card.hb.hovered) {
            card.drawScale = card.targetDrawScale = offerHoveredScale;
            hoveringCard = true;
        } else {
            card.drawScale = card.targetDrawScale = offerScale;
            hoveringCard = false;
        }
    }
    
    private static void updateHitboxLogic() {
        for (int i = 0; i < maxSlot; i++) {
            hbs[i].update();
            if (hbs[i].hovered) {
                if ((offers.group.size() - 1 < i || (offers.group.size() - 1 >= i && offers.group.get(i) == null))) 
                    TipHelper.renderGenericTip(hbs[i].x + 90F * Settings.xScale, hbs[i].y - 20F * Settings.yScale, TEXT[0], TEXT[1]);
                else if (offers.group.size() - 1 >= i && offers.group.get(i) != null) {
                    PowerTip tip = constructDescription(offers.group.get(i), OfferLogics.get(offers.group.get(i)));
                    TipHelper.renderGenericTip(hbs[i].x + (hoveringCard ? 100F : 90F) * Settings.xScale, 
                            hbs[i].y - (hoveringCard ? 30F : 20F) * Settings.yScale, tip.header, tip.body);
                }
            }
        }
    }
    
    @NotNull
    @Contract("_, _ -> new")
    private static PowerTip constructDescription(@NotNull AbstractCard card, int turnsLeft) {
        StringBuilder body = new StringBuilder(card.name);
        String header = TEXT[2] + card.name;
        if (turnsLeft > 1 && !(card instanceof TotemOffering)) {
            body.append(TEXT[3]).append(turnsLeft).append(TEXT[5]);
        } else if (!(card instanceof TotemOffering)) {
            body.append(TEXT[4]);
        } else if (!((TotemOffering) card).isIndestructible()) {
            body.append(TEXT[7]);
        } else if (((TotemOffering) card).isIndestructible()) {
            body.append(TEXT[8]);
            return new PowerTip(header, body.toString());
        }
        if (sideEffects == null) {
            NesDebug.Log("????????????????????????????????");
        }
        for (OfferingSideEffect<AbstractCard, Consumer<AbstractCard>, OfferingSideEffectDescription> effect : sideEffects) {
            if (effect.identify(card) && effect.find(card) != null) {
                body.append((card instanceof TotemOffering) ? TEXT[9] : TEXT[6]).append(effect.find(card).apply(card));
                break;
            }
        }
        return new PowerTip(header, body.toString());
    }
    
    public static void updatePostTurnStartLogic() {
        List<AbstractCard> toRemove = new ArrayList<>();
        for (AbstractCard card : offers.group) {
            if (card instanceof TotemOffering) continue;
            if (!OfferLogics.containsKey(card)) continue;
            int turnsLeft = OfferLogics.get(card);
            if (turnsLeft > 1) {
                turnsLeft--;
                OfferLogics.replace(card, turnsLeft);
            } else if (!toRemove.contains(card)) {
                NesDebug.Log("Removing " + card.name + " from offer slots, it's done.");
                toRemove.add(card);
                OfferLogics.remove(card, OfferLogics.get(card));
                hbs[offers.group.indexOf(card)].setOccupied(false);
            }
        }
        if (!toRemove.isEmpty()) {
            for (AbstractCard card : toRemove) {
                if (!(card instanceof DivineOffering)) {
                    if (card instanceof AbstractOffering) {
                        card.superFlash(Color.ORANGE.cpy());
                        ((AbstractOffering) card).triggerOnInitializationAndCompletion();
                        ((AbstractOffering) card).onOfferCompleted();
                        if (card instanceof UndeadOffering)
                            ((UndeadOffering) card).triggerUndeadEffect();
                    }
                    offers.moveToExhaustPile(card);
                } else {
                    ((DivineOffering) card).triggerDivineEffect();
                    offers.removeCard(card);
                }
                CardsOfferedThisCombat.add(card);
                contactCardsOnOfferingExhausted(card);
                List<OfferingSideEffect<AbstractCard, Consumer<AbstractCard>, OfferingSideEffectDescription>> remove = new ArrayList<>();
                for (OfferingSideEffect<AbstractCard, Consumer<AbstractCard>, OfferingSideEffectDescription> effect : sideEffects) {
                    if (effect.identify(card)) {
                        remove.add(effect);
                        if (effect.accept(card) != null) {
                            effect.accept(card).accept(card);
                        }
                    }
                }
                sideEffects.removeAll(remove);
            }
            AdjustOfferingSlots();
        }
        PrefabMgr.cleanAfterJobsDone();
    }
    
    public static boolean DestroyOffering(AbstractCard card) {
        if (!offers.contains(card)) return false;
        boolean canDestroy = true;
        if (card instanceof TotemOffering)
            canDestroy = !((TotemOffering) card).isIndestructible();
        if (canDestroy) {
            hbs[offers.group.indexOf(card)].setOccupied(false);
            offers.removeCard(card);
            boolean success = OfferLogics.remove(card, OfferLogics.get(card));
            if (success) {
                if (card instanceof UndeadOffering)
                    ((UndeadOffering) card).triggerUndeadEffect();
                if (card instanceof DivineOffering)
                    ((DivineOffering) card).triggerDivineEffect();
                contactCardsOnOfferingExhausted(card);
                AdjustOfferingSlots();
                return true;
            }
        } else {
            NesDebug.Log(card, "Indestructible totem...");
        }
        return false;
    }
    
    public static void RemoveAllOfferings() {
        List<AbstractCard> tmp = new ArrayList<>(offers.group);
        for (AbstractCard card : tmp) {
            if (offers.group.contains(card)) {
                hbs[offers.group.indexOf(card)].setOccupied(false);
                offers.removeCard(card);
            }
        }
        OfferLogics.clear();
        AdjustOfferingSlots();
    }
    
    private static void contactCardsOnOfferingExhausted(AbstractCard offering) {
        for (AbstractCard c : cpr().hand.group) {
            if (c instanceof OfferingContacter)
                ((OfferingContacter) c).onOfferingExhausted(offering);
        }
        for (AbstractCard c : cpr().drawPile.group) {
            if (c instanceof OfferingContacter)
                ((OfferingContacter) c).onOfferingExhausted(offering);
        }
        for (AbstractCard c : cpr().discardPile.group) {
            if (c instanceof OfferingContacter)
                ((OfferingContacter) c).onOfferingExhausted(offering);
        }
        for (AbstractCard c : cpr().exhaustPile.group) {
            if (c instanceof OfferingContacter)
                ((OfferingContacter) c).onOfferingExhausted(offering);
        }
    }
    
    public static void AdjustOfferingSlots() {
        List<AbstractCard> tmp = new ArrayList<>();
        for (int i = 0; i < maxSlot; i++) {
            if (offers.size() - 1 >= i && offers.group.get(i) != null)
                tmp.add(offers.group.get(i));
        }
        if (!tmp.isEmpty()) {
            offers.clear();
            for (OfferingHitbox hitbox : hbs)
                hitbox.setOccupied(false);
            for (int i = 0; i < tmp.size(); i++) {
                AbstractCard card = tmp.get(i);
                if (!offers.contains(card)) {
                    offers.addToTop(card);
                    card.target_x = slots[i].x + CARDX_OFFSET;
                    hbs[i].setOccupied(true);
                }
            }
            tmp.clear();
        }
        PrefabMgr.cleanAfterJobsDone();
    }
    
    @NotNull
    @Contract(pure = true)
    public static CardGroup GetOfferingPack() {
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (!offers.isEmpty()) {
            for (AbstractCard card : offers.group) {
                tmp.addToBottom(card.makeStatEquivalentCopy());
            }
        }
        return tmp;
    }
    
    public static CardGroup GetCardsOfferedThisCombat() {
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (!CardsOfferedThisCombat.isEmpty()) {
            for (AbstractCard card : CardsOfferedThisCombat) {
                tmp.addToBottom(card.makeStatEquivalentCopy());
            }
        }
        return tmp;
    }
    
    public static boolean CanOffer() {
        return valid && cpr() instanceof TheNemesis && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }
    
    public static boolean HasOffering(AbstractCard card) {
        return offers.contains(card);
    }
    
    public static boolean HasOfferings(List<AbstractCard> cards) {
        return !Collections.disjoint(offers.group, cards);
    }
    
    private static float adjustFirstSlotX() {
        float initPosition = cpr().drawX - STARTX_OFFSET;
        boolean half = maxSlot % 2 == 0;
        return maxSlot > 1 ? initPosition - (half ? 43F * maxSlot : 86F * (maxSlot / 2F)) * Settings.xScale : initPosition;
    }
    
    private static AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }
}