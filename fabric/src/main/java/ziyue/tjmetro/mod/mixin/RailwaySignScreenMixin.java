package ziyue.tjmetro.mod.mixin;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mod.data.IGui;
import org.mtr.mod.screen.DashboardListItem;
import org.mtr.mod.screen.DashboardListSelectorScreen;
import org.mtr.mod.screen.RailwaySignScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.mod.block.base.IRailwaySign;

import javax.annotation.Nullable;

/**
 * @author ZiYueCommentary
 * @see RailwaySignScreen
 * @since 1.0.0-beta-1
 */

@Mixin(RailwaySignScreen.class)
public abstract class RailwaySignScreenMixin extends ScreenExtension implements IGui
{
    @Shadow(remap = false)
    private int editingIndex;

    @Shadow(remap = false)
    @Final
    private String[] signIds;

    @Shadow(remap = false)
    @Final
    private ObjectImmutableList<DashboardListItem> exitsForList;

    @Shadow(remap = false)
    @Final
    private ObjectImmutableList<DashboardListItem> platformsForList;

    @Shadow(remap = false)
    @Final
    private ObjectArraySet<DashboardListItem> routesForList;

    @Shadow(remap = false)
    @Final
    private ObjectArraySet<DashboardListItem> stationsForList;

    @Shadow(remap = false)
    @Final
    private LongAVLTreeSet selectedIds;

    /**
     * @author ZiYueCommentary
     * @reason Dynamic signs from Tianjin Metro.
     */
    @Overwrite(remap = false)
    private void setNewSignId(@Nullable String newSignId) {
        if (editingIndex >= 0 && editingIndex < signIds.length) {
            signIds[editingIndex] = newSignId;
            final boolean isExitLetter = IRailwaySign.signIsExit(newSignId);
            final boolean isPlatform = IRailwaySign.signIsPlatform(newSignId);
            final boolean isLine = IRailwaySign.signIsLine(newSignId);
            final boolean isStation = IRailwaySign.signIsStation(newSignId);
            if ((isExitLetter || isPlatform || isLine || isStation)) {
                MinecraftClient.getInstance().openScreen(new Screen(new DashboardListSelectorScreen(new ObjectImmutableList<>(isExitLetter ? exitsForList : isPlatform ? platformsForList : isLine ? routesForList : stationsForList), selectedIds, false, false, this)));
            }
        }
    }
}
