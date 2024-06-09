package ziyue.tjmetro.mixin;

import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.mappings.ScreenMapper;
import mtr.mappings.UtilitiesClient;
import mtr.screen.DashboardListSelectorScreen;
import mtr.screen.RailwaySignScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.block.base.IRailwaySign;

import java.util.List;
import java.util.Set;

@Mixin(RailwaySignScreen.class)
public abstract class RailwaySignScreenMixin extends ScreenMapper implements IGui
{
    @Shadow(remap = false)
    private int editingIndex;

    @Shadow(remap = false)
    @Final
    private String[] signIds;

    @Shadow(remap = false)
    @Final
    private List<NameColorDataBase> exitsForList;

    @Shadow(remap = false)
    @Final
    private List<NameColorDataBase> platformsForList;

    @Shadow(remap = false)
    @Final
    private List<NameColorDataBase> routesForList;

    @Shadow(remap = false)
    @Final
    private List<NameColorDataBase> stationsForList;

    @Shadow(remap = false)
    @Final
    private Set<Long> selectedIds;

    protected RailwaySignScreenMixin(Component title) {
        super(title);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private void setNewSignId(String newSignId) {
        if (editingIndex >= 0 && editingIndex < signIds.length) {
            signIds[editingIndex] = newSignId;
            final boolean isExitLetter = IRailwaySign.signIsExit(newSignId);
            final boolean isLine = IRailwaySign.signIsLine(newSignId);
            final boolean isPlatform = IRailwaySign.signIsPlatform(newSignId);
            final boolean isStation = IRailwaySign.signIsStation(newSignId);
            if ((isExitLetter || isPlatform || isLine || isStation) && minecraft != null) {
                UtilitiesClient.setScreen(minecraft, new DashboardListSelectorScreen(this, isExitLetter ? exitsForList : isPlatform ? platformsForList : isLine ? routesForList : stationsForList, selectedIds, false, false));
            }
        }
    }
}
