package rs.prefabs.general.utils;

import com.badlogic.gdx.graphics.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PrefabUtils {

    @NotNull
    @Contract("_, _, _ -> new")
    public static Color quickColor(int r, int g, int b) {
        return quickColor(r, g, b, 255);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static Color quickColor(int r, int g, int b, int a) {
        return quickColor(r, g, b, a / 255F);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static Color quickColor(int r, int g, int b, float a) {
        return quickColor(r * 1F, g * 1F, b * 1F, a);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static Color quickColor(float r, float g, float b, float a) {
        return new Color(r / 255F, g / 255F, b / 255F, a);
    }
    
    @NotNull
    public static Color complement(@NotNull Color color) {
        float r = color.r * 255F;
        float g = color.g * 255F;
        float b = color.b * 255F;
        return quickColor(255F - r, 255F - g, 255F - b, color.a);
    }

    public static Color contrast(@NotNull Color color) {
        return contrast(color, 127.5F);
    }

    public static Color contrast(@NotNull Color color, float deg) {
        if (deg >= 255F)
            return complement(color);
        float r = color.r * 255F;
        float g = color.g * 255F;
        float b = color.b * 255F;
        return quickColor(Math.abs(deg - r), Math.abs(deg - g), Math.abs(deg - b), color.a);
    }

    @NotNull
    public static Color blend(@NotNull Color c1, @NotNull Color c2) {
        float[] value1 = new float[] {c1.r * 255F, c1.g * 255F, c1.b * 255F};
        float[] value2 = new float[] {c2.r * 255F, c2.g * 255F, c2.b * 255F};
        float[] powers = new float[] {c1.a, c2.a};
        float a = calculateBlendPower(powers[0], powers[1]);
        float r = calculateBlendChannel(powers[0], powers[1], value1[0], value2[0]);
        float g = calculateBlendChannel(powers[0], powers[1], value1[1], value2[1]);
        float b = calculateBlendChannel(powers[0], powers[1], value1[2], value2[2]);
        return quickColor(r, g, b, a);
    }

    public static float calculateBlendPower(float a1, float a2) {
        return a1 + a2 - a1 * a2;
    }

    public static float calculateBlendChannel(float a1, float a2, float c1, float c2) {
        return (c1 * a1 * (1F - a2) + c2 * a2) / (calculateBlendPower(a1, a2));
    }

    public static double SciRound(double a, double reserved) {
        long bH = (long) a;
        long epd = (long) Math.pow(10, reserved);
        long nH = (long) ((a - bH) * epd);
        float nT = (float) ((a - bH) * epd - nH);
        int hT = (int) (nH % 10);
        int tH = (int) (nT * 10);
        if (tH > 5) {
            nH++;
        }
        else if (tH == 5) {
            if (hT % 2 != 0) nH++;
        }

        return bH + (double) nH / epd;
    }
}