package rs.prefabs.nemesis.cards.generals;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;
import rs.prefabs.general.actions.common.NullableSrcDamageAction;
import rs.prefabs.general.actions.utility.DelayAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Soultaker extends AbstractNesGeneralCard {
    private final List<String> memorizedClasses = new ArrayList<>();
    
    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {
        if (!memorizedClasses.isEmpty() && memorizedClasses.contains(t.getClass().getName())) {
            addToBot(new DelayAction(() -> {
                for (AbstractCard card : cpr().masterDeck.group) {
                    if (card.uuid.equals(Soultaker.this.uuid)) {
                        card.misc += magicNumber;
                        card.applyPowers();
                        card.baseDamage = card.misc;
                        card.isDamageModified = false;
                    }
                }
                for (AbstractCard card : GetAllInBattleInstances.get(Soultaker.this.uuid)) {
                    card.misc += magicNumber;
                    card.baseDamage = card.misc;
                    card.applyPowers();
                }
            }));
        } else if (!memorizedClasses.contains(t.getClass().getName())) {
            addToBot(new DelayAction(() -> memorizedClasses.add(t.getClass().getName())));
        }
        addToBot(new NullableSrcDamageAction(t, crtDmgInfo(s, damage, damageTypeForTurn), 
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new VFXAction(t, new VerticalAuraEffect(Color.BLACK.cpy(), t.hb.cX, t.hb.cY), 0F));
    }

    @Override
    protected void spectralize() {
        enchantDamage(0, baseDamage + 2);
        enchantMagics(1, baseMagicNumber + 4);
    }

    @Override
    protected void despectralize() {
        disenchantDamage(0);
        disenchantMagics(1);
    }

    @Override
    protected Map<String, String> createCustomMapOnSaving() {
        Map<String, String> map = new HashMap<>();
        int num = 0;
        for (String str : memorizedClasses) {
            if (str != null) {
                map.put("Soultaker_" + num, str);
                num++;
            }
        }
        return map;
    }

    @Override
    protected void receiveCustomMapOnLoading(Map<String, String> map) {
        if (map == null) {
            Log("Unable to find soultaker's memorized list");
            return;
        }
        memorizedClasses.clear();
        int num = 0;
        while (num < map.size()) {
            if (map.containsKey("Soultaker_" + num)) {
                Log("Retriving saved classes...");
                memorizedClasses.add(map.get("Soultaker_" + num));
            }
            num++;
        }
    }
}