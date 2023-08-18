package ziyue.tjmetro.mixin.mixins;

import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.screen.ConfigScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.mixin.properties.ConfigScreenProperties;

/**
 * A part of "Experimental MTR config screen".
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.Config
 * @see ConfigScreenProperties
 * @see MinecraftMixin
 * @since beta-1
 */

@Mixin(ConfigScreen.class)
public abstract class ConfigScreenMixin extends ScreenMapper implements IGui, ConfigScreenProperties
{
    @Final
    @Shadow(remap = false)
    private boolean hasTimeAndWindControls;

    @Final
    @Shadow(remap = false)
    private boolean useTimeAndWindSync;

    protected ConfigScreenMixin(Component title) {
        super(title);
    }

    @Override
    public boolean hasTimeAndWindControls() {
        return hasTimeAndWindControls;
    }

    @Override
    public boolean useTimeAndWindSync() {
        return useTimeAndWindSync;
    }
}
