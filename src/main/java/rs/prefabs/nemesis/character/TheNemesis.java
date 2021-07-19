package rs.prefabs.nemesis.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.vfx.NemesisFireParticle;
import rs.prefabs.general.abstracts.AbstractPrefabCharacter;
import rs.prefabs.nemesis.NesFab;
import rs.prefabs.nemesis.cards.AbstractNesCard;
import rs.prefabs.nemesis.cards.generals.*;
import rs.prefabs.nemesis.patches.NesCardEnum;
import rs.prefabs.nemesis.relics.OldCenser;
import rs.prefabs.nemesis.ui.panels.NemesisEnergyOrb;

import java.util.ArrayList;

public class TheNemesis extends AbstractPrefabCharacter {
    public static final String ID = "TheNemesis";
    public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    public static final String NAME = charStrings.NAMES[0];
    public static final String DESCRIPTION = charStrings.TEXT[0];
    public static final String Shoulder_1 = "PrefabsAssets/NemesisProperties/images/charSets/shoulder.png";
    public static final String Shoulder_2 = "PrefabsAssets/NemesisProperties/images/charSets/shoulder2.png";
    public static final String corpse = "PrefabsAssets/NemesisProperties/images/charSets/corpse.png";
    public static final String SK_ALT = "PrefabsAssets/NemesisProperties/images/charSets/anim/skeleton.atlas";
    public static final String SK_JSON = "PrefabsAssets/NemesisProperties/images/charSets/anim/skeleton.json";
    public static final int STARTING_HP = 60;
    public static final int MAX_HP = 60;
    public static final int STARTING_GOLD = 99;
    public static final int ORB_SLOTS = 0;
    public static final int DRAW_PER_TURN = 5;
    public static final Color NEMESIS_COLOR = Color.OLIVE.cpy();
    private final NemesisEnergyOrb energyOrb = new NemesisEnergyOrb();
    private float fireTimer = 0.0F;
    private Bone eye1;
    private Bone eye2;
    private Bone eye3;

    public TheNemesis() {
        super("The Nemesis", Enum.THE_NEMESIS);
        initializeClass(null, Shoulder_2, Shoulder_1, corpse, this.getLoadout(),
                20F, -10F, 300.0F, 370.0F, new EnergyManager(3));
        loadAnimation(SK_ALT, SK_JSON, 1.25F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(0.8F);
        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);
        this.eye1 = this.skeleton.findBone("eye0");
        this.eye2 = this.skeleton.findBone("eye1");
        this.eye3 = this.skeleton.findBone("eye2");
    }

    public static class Enum {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_NEMESIS;
    }

    @Override
    public void useFastAttackAnimation() {
        try {
            changeState("Attack");
        } catch (Exception e) {
            e.printStackTrace();
            super.useFastAttackAnimation();
        }
    }

    @Override
    public void useStaggerAnimation() {
        try {
            changeState("Hit");
        } catch (Exception e) {
            super.useStaggerAnimation();
        }
    }

    private void playRandomAtkSfx() {
        if (MathUtils.randomBoolean()) {
            playSound(NesFab.makeID("Nemesis_Atk_v1"));
        } else {
            playSound(NesFab.makeID("Nemesis_Atk_v2"));
        }
    }

    public void changeState(String key) {
        try {
            switch (key) {
                case "Attack":
                    playRandomAtkSfx();
                    this.state.setAnimation(0, "Attack", false);
                    this.state.addAnimation(0, "Idle", true, 0F);
                    break;
                case "Hit":
                    this.state.setAnimation(0, "Hit", false);
                    this.state.addAnimation(0, "Idle", true, 0F);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void preBattlePrep() {
        super.preBattlePrep();
        OfferHelper.initPreBattle();
    }

    @Override
    public void update() {
        super.update();
        if (!this.isDying) {
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            if (this.fireTimer < 0.0F) {
                this.fireTimer = 0.05F;
                AbstractDungeon.effectList.add(new NemesisFireParticle(this.skeleton
                        .getX() + this.eye1.getWorldX(), this.skeleton.getY() + this.eye1.getWorldY()));
                AbstractDungeon.effectList.add(new NemesisFireParticle(this.skeleton
                        .getX() + this.eye2.getWorldX(), this.skeleton.getY() + this.eye2.getWorldY()));
                AbstractDungeon.effectList.add(new NemesisFireParticle(this.skeleton
                        .getX() + this.eye3.getWorldX(), this.skeleton.getY() + this.eye3.getWorldY()));
            }
        }
    }

    @Override
    public void applyStartOfTurnPostDrawPowers() {
        super.applyStartOfTurnPostDrawPowers();
        OfferHelper.updatePostTurnStartLogic();
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> starters = new ArrayList<>();
        starters.add(FlashStrike.ID);
        starters.add(FlashStrike.ID);
        starters.add(FlashStrike.ID);
        starters.add(FlashStrike.ID);
        starters.add(FlashStrike.ID);
        starters.add(IllusiveShield.ID);
        starters.add(IllusiveShield.ID);
        starters.add(IllusiveShield.ID);
        starters.add(IllusiveShield.ID);
        starters.add(Scorch.ID);
        starters.add(Metamorphose.ID);
        starters.add(Metamorphose.ID);
        starters.add(SurrealMemoir.ID);
        return starters;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> starters = new ArrayList<>();
        starters.add(OldCenser.ID);
        return starters;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, DESCRIPTION, STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, DRAW_PER_TURN,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return NesCardEnum.Nemesis;
    }

    @Override
    public Color getCardRenderColor() {
        return NEMESIS_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new SurrealMemoir();
    }

    @Override
    public Color getCardTrailColor() {
        return NEMESIS_COLOR;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontGreen;
    }

    @Override
    public void updateOrb(int energyCount) {
        energyOrb.updateOrb(energyCount);
    }

    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        String key = null;
        if (hoveredCard instanceof AbstractNesCard) {
            
        } else if (cardInUse instanceof AbstractNesCard) {
            
        }
        energyOrb.renderOrb(sb, enabled, current_x, current_y);
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        playAdjustedSound(NesFab.makeID("Nemesis_Awk"), MathUtils.random(0.1F, 0.3F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return NesFab.makeID("Nemesis_Awk");
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new TheNemesis();
    }

    @Override
    public String getSpireHeartText() {
        return charStrings.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
        return NEMESIS_COLOR;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, 
                AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, 
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        };
    }

    @Override
    public String getVampireText() {
        return charStrings.TEXT[2];
    }
}
