package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mod.Init;
import ziyue.tjmetro.mod.Reference;

/**
 * Prebuilt texture identifiers used by the door renderers' hot path.
 *
 * @author Codex
 * @since 1.2.0-alpha
 */
public final class DoorTextureCache
{
    private static final Identifier[][] PSD = matrix("textures/block/psd_door_tianjin_%s_%s.png");
    private static final Identifier[][] PSD_END_1 = matrix("textures/block/psd_door_end_tianjin_%s_%s_1.png");
    private static final Identifier[][] PSD_END_2 = matrix("textures/block/psd_door_end_tianjin_%s_%s_2.png");
    private static final Identifier[][] APG = matrix("textures/block/apg_door_tianjin_%s_%s.png");
    private static final Identifier[][] TRT = matrix("textures/block/apg_door_tianjin_trt_%s_%s.png");
    private static final Identifier[] PSD_LIGHT = pair("textures/block/light_off.png", "textures/block/light_on.png");
    private static final Identifier[] APG_LIGHT = pair("textures/block/apg_door_tianjin_light_off.png", "textures/block/apg_door_tianjin_light_on.png");
    private static final Identifier[] JINJING_LIGHT = pair("textures/block/apg_door_tianjin_jinjing_light_off.png", "textures/block/apg_door_tianjin_jinjing_light_on.png");
    private static final Identifier LOCKED = new Identifier(Init.MOD_ID, "textures/block/sign/door_not_in_use.png");

    private DoorTextureCache() {
    }

    public static Identifier psd(boolean half, boolean side) {
        return PSD[index(half)][index(side)];
    }

    public static Identifier psdEnd1(boolean half, boolean side) {
        return PSD_END_1[index(half)][index(side)];
    }

    public static Identifier psdEnd2(boolean half, boolean side) {
        return PSD_END_2[index(half)][index(side)];
    }

    public static Identifier apg(boolean half, boolean side) {
        return APG[index(half)][index(side)];
    }

    public static Identifier trt(boolean half, boolean side) {
        return TRT[index(half)][index(side)];
    }

    public static Identifier psdLight(boolean open) {
        return PSD_LIGHT[index(open)];
    }

    public static Identifier apgLight(boolean open) {
        return APG_LIGHT[index(open)];
    }

    public static Identifier jinjingLight(boolean open) {
        return JINJING_LIGHT[index(open)];
    }

    public static Identifier locked() {
        return LOCKED;
    }

    private static Identifier[][] matrix(String template) {
        return new Identifier[][]{
                {new Identifier(Reference.MOD_ID, String.format(template, "bottom", "left")), new Identifier(Reference.MOD_ID, String.format(template, "bottom", "right"))},
                {new Identifier(Reference.MOD_ID, String.format(template, "top", "left")), new Identifier(Reference.MOD_ID, String.format(template, "top", "right"))}
        };
    }

    private static Identifier[] pair(String off, String on) {
        return new Identifier[]{new Identifier(Reference.MOD_ID, off), new Identifier(Reference.MOD_ID, on)};
    }

    private static int index(boolean value) {
        return value ? 1 : 0;
    }
}
