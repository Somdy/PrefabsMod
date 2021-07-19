package rs.prefabs.nemesis.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import org.jetbrains.annotations.NotNull;
import rs.prefabs.general.interfaces.TemplateItem;
import rs.prefabs.nemesis.data.NesDataReader;

import java.util.function.Consumer;

public class NesOptionCard extends AbstractNesCard implements TemplateItem {
    private Consumer<AbstractCard> onChooseThis;
    private Color favorColor;
    private int multi;
    
    public NesOptionCard() {
        super(NesDataReader.getGeneralCardData());
        onChooseThis = null;
        favorColor = Color.WHITE.cpy();
        multi = 1;
    }

    @NotNull
    public static NesOptionCard createOption(String name, String description, String img, boolean displayUpgraded, boolean displayEnchanted,
                                             Color favorColor, Consumer<AbstractCard> onChooseThis) {
        return createOption(name, description, img, displayUpgraded, displayEnchanted, null,
                0, 0, 0, 0, favorColor, onChooseThis);
    }

    @NotNull
    public static NesOptionCard createOption(String name, String description, String img, boolean displayUpgraded, boolean displayEnchanted,
                                             AbstractCard cardToPreview, Color favorColor, Consumer<AbstractCard> onChooseThis) {
        return createOption(name, description, img, displayUpgraded, displayEnchanted, cardToPreview, 
                0, 0, 0, 0, favorColor, onChooseThis);
    }
    
    @NotNull
    public static NesOptionCard createOption(String name, String description, String img, boolean displayUpgraded, boolean displayEnchanted,
                                             AbstractCard cardToPreview, int damage, int block, int magics, int exmagics,
                                             Color favorColor, Consumer<AbstractCard> onChooseThis) {
        NesOptionCard option = new NesOptionCard();
        option.setDamageValue(damage, true);
        option.setBlockValue(block, true);
        option.setMagicValue(magics, true);
        option.setExMagicNumValue(exmagics, true);
        option.enchanted = displayEnchanted;
        if (displayUpgraded) {
            option.upgradeName(name);
        } else {
            option.name = name;
            option.initializeTitle();
        }
        option.upgradeDescription(description);
        option.textureImg = img;
        option.loadCardImage(option.textureImg);
        if (cardToPreview != null) option.cardsToPreview = cardToPreview;
        if (favorColor != null) option.favorColor = favorColor;
        option.onChooseThis = onChooseThis;
        return option;
    }

    public NesOptionCard setMultiplier(int multiplier) {
        this.multi = multiplier;
        return this;
    }

    @Override
    public void onChoseThisOption() {
        if (onChooseThis != null) {
            for (int i = 0; i < multi; i++) 
                onChooseThis.accept(this);
        }
    }

    @Override
    public boolean canEnemyUse() {
        return false;
    }

    @Override
    protected void play(AbstractCreature s, AbstractCreature t) {}

    @Override
    public void promote() {}

    @Override
    protected void spectralize() {}

    @Override
    protected void despectralize() {}

    @Override
    protected String getKey() {
        return TEMPLATE_KEY;
    }

    @NotNull
    @Override
    public Color getFavoriteColor() {
        return favorColor;
    }
}