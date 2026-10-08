package ziyue.tjmetro.mod.mixin;

import org.mtr.core.data.Station;
import org.mtr.core.operation.NearbyAreasRequest;
import org.mtr.core.operation.NearbyAreasResponse;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.data.TicketSystem;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ziyue.tjmetro.mod.block.BlockTicketBarrierOutboundTransfer;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.function.Consumer;

/**
 * A part of outbound transfer feature.
 *
 * @author ZiYueCommentary
 * @see TicketSystem
 * @since 1.1.3
 */
@Mixin(TicketSystem.class)
public abstract class TicketSystemMixin {
    @Shadow(remap = false)
    private static void setPlayerScore(World world, PlayerEntity player, String objective, String title, int value) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow(remap = false)
    private static int getPlayerScore(World world, PlayerEntity player, String objective, String title) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow(remap = false)
    @Final
    private static String ENTRY_ZONE_1_OBJECTIVE;
    @Shadow(remap = false)
    @Final
    private static String ENTRY_ZONE_1_TITLE;
    @Shadow(remap = false)
    @Final
    private static String ENTRY_ZONE_2_OBJECTIVE;
    @Shadow(remap = false)
    @Final
    private static String ENTRY_ZONE_2_TITLE;
    @Shadow(remap = false)
    @Final
    private static String ENTRY_ZONE_3_OBJECTIVE;
    @Shadow(remap = false)
    @Final
    private static String ENTRY_ZONE_3_TITLE;

    @Shadow(remap = false)
    private static boolean entered(int entryZone1, int entryZone2, int entryZone3) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow(remap = false)
    private static int decodeZone(int zone) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow(remap = false)
    private static boolean isConcessionary(PlayerEntity player) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow(remap = false)
    private static void incrementPlayerScore(World world, PlayerEntity player, String objective, String title, int value) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow(remap = false)
    @Final
    public static String BALANCE_OBJECTIVE;
    @Shadow(remap = false)
    @Final
    public static String BALANCE_OBJECTIVE_TITLE;

    @Shadow(remap = false)
    private static String formatStationName(Station station) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow(remap = false)
    private static boolean onExit(World world, Station station, PlayerEntity player, boolean remindIfNoRecord) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow(remap = false)
    private static boolean onEnter(World world, Station station, PlayerEntity player, boolean remindIfNoRecord) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Unique
    private static final String OUTBOUND_TRANSFER_STATION_OBJECTIVE = "tjmetro_outbound_transfer_station";
    @Unique
    private static final String OUTBOUND_TRANSFER_STATION_TITLE = "Outbound Transfer Station";
    @Unique
    private static final String OUTBOUND_TRANSFER_TIME_OBJECTIVE = "tjmetro_outbound_transfer_time";
    @Unique
    private static final String OUTBOUND_TRANSFER_TIME_TITLE = "Outbound Transfer Time";

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public static void passThrough(World world, BlockPos blockPos, PlayerEntity player, boolean isEntrance, boolean isExit, SoundEvent entrySound, SoundEvent entrySoundConcessionary, SoundEvent exitSound, SoundEvent exitSoundConcessionary, @Nullable SoundEvent failSound, boolean remindIfNoRecord, Consumer<TicketSystem.EnumTicketBarrierOpen> callback) {
        Init.sendMessageC2S("nearby_stations", world.getServer(), world, new NearbyAreasRequest<>(Init.blockPosToPosition(blockPos), 0L), (nearbyAreasResponse) -> {
            ObjectImmutableList<Station> stations = nearbyAreasResponse.getStations();
            if (stations.isEmpty()) {
                callback.accept(TicketSystem.EnumTicketBarrierOpen.CLOSED);
            } else {
                Station station = stations.get(0);
                boolean isEntering;
                if (isEntrance && isExit) {
                    isEntering = !entered(getPlayerScore(world, player, "mtr_entry_zone_1", "Entry Zone 1"), getPlayerScore(world, player, "mtr_entry_zone_2", "Entry Zone 2"), getPlayerScore(world, player, "mtr_entry_zone_3", "Entry Zone 3"));
                } else {
                    isEntering = isEntrance;
                }

                boolean canOpen;
                if (isEntering) {
                    canOpen = onEnter(world, station, player, remindIfNoRecord);
                } else {
                    if (world.getBlockState(blockPos).getBlock().data instanceof BlockTicketBarrierOutboundTransfer) {
                        setPlayerScore(world, player, OUTBOUND_TRANSFER_STATION_OBJECTIVE, OUTBOUND_TRANSFER_STATION_TITLE, (int) station.getId());
                        setPlayerScore(world, player, OUTBOUND_TRANSFER_TIME_OBJECTIVE, OUTBOUND_TRANSFER_TIME_TITLE, (int) (Instant.now().getEpochSecond() / 60));
                        player.sendMessage(Text.cast(TextHelper.translatable("gui.tjmetro.outbound_transfer_exit")), true);
                        canOpen = true;
                    } else {
                        setPlayerScore(world, player, OUTBOUND_TRANSFER_STATION_OBJECTIVE, OUTBOUND_TRANSFER_STATION_TITLE, 0);
                        setPlayerScore(world, player, OUTBOUND_TRANSFER_TIME_OBJECTIVE, OUTBOUND_TRANSFER_TIME_TITLE, 0);
                        canOpen = onExit(world, station, player, remindIfNoRecord);
                    }
                }

                if (canOpen) {
                    world.playSound(null, blockPos, isConcessionary(player) ? (isEntering ? entrySoundConcessionary : exitSoundConcessionary) : (isEntering ? entrySound : exitSound), SoundCategory.BLOCKS, 1.0F, 1.0F);
                } else if (failSound != null) {
                    world.playSound(null, blockPos, failSound, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                callback.accept(canOpen ? (isConcessionary(player) ? TicketSystem.EnumTicketBarrierOpen.OPEN_CONCESSIONARY : TicketSystem.EnumTicketBarrierOpen.OPEN) : TicketSystem.EnumTicketBarrierOpen.CLOSED);
            }

        }, NearbyAreasResponse.class);
    }

    @Inject(method = "onEnter", at = @At("HEAD"), remap = false, cancellable = true)
    private static void beforeOnEnter(World world, Station station, PlayerEntity player, boolean remindIfNoRecord, CallbackInfoReturnable<Boolean> cir) {
        int outboundTransferStation = getPlayerScore(world, player, OUTBOUND_TRANSFER_STATION_OBJECTIVE, OUTBOUND_TRANSFER_STATION_TITLE);
        int outboundTransferTime = getPlayerScore(world, player, OUTBOUND_TRANSFER_TIME_OBJECTIVE, OUTBOUND_TRANSFER_TIME_TITLE);
        if (outboundTransferTime == 0) return;

        int now = (int) Instant.now().getEpochSecond() / 60;
        if (outboundTransferStation != (int) station.getId() || now - outboundTransferTime > 10) {
            int entryZone1 = getPlayerScore(world, player, ENTRY_ZONE_1_OBJECTIVE, ENTRY_ZONE_1_TITLE);
            int entryZone2 = getPlayerScore(world, player, ENTRY_ZONE_2_OBJECTIVE, ENTRY_ZONE_2_TITLE);
            int entryZone3 = getPlayerScore(world, player, ENTRY_ZONE_3_OBJECTIVE, ENTRY_ZONE_3_TITLE);
            boolean entered = entered(entryZone1, entryZone2, entryZone3);
            if (!entered && remindIfNoRecord) {
                player.sendMessage(TranslationProvider.GUI_MTR_ALREADY_EXITED.getText(), true);
            } else {
                long fare = 5L + (Math.abs(station.getZone1() - (long) decodeZone(entryZone1)) + Math.abs(station.getZone2() - (long) decodeZone(entryZone2)) + Math.abs(station.getZone3() - (long) decodeZone(entryZone3)));
                long finalFare = entered ? (isConcessionary(player) ? (long) Math.ceil((float) fare / 2.0F) : fare) : 500L;
                setPlayerScore(world, player, ENTRY_ZONE_1_OBJECTIVE, ENTRY_ZONE_1_TITLE, 0);
                setPlayerScore(world, player, ENTRY_ZONE_2_OBJECTIVE, ENTRY_ZONE_2_TITLE, 0);
                setPlayerScore(world, player, ENTRY_ZONE_3_OBJECTIVE, ENTRY_ZONE_3_TITLE, 0);
                incrementPlayerScore(world, player, BALANCE_OBJECTIVE, BALANCE_OBJECTIVE_TITLE, (int) (-finalFare));
                setPlayerScore(world, player, OUTBOUND_TRANSFER_STATION_OBJECTIVE, OUTBOUND_TRANSFER_STATION_TITLE, 0);
                setPlayerScore(world, player, OUTBOUND_TRANSFER_TIME_OBJECTIVE, OUTBOUND_TRANSFER_TIME_TITLE, 0);

                player.sendMessage(Text.cast(TextHelper.translatable(now - outboundTransferTime > 10 ? "gui.tjmetro.outbound_transfer_expired" : "gui.tjmetro.outbound_transfer_invalid", formatStationName(station), finalFare)), true);
            }
            cir.setReturnValue(true);
            return;
        }

        setPlayerScore(world, player, OUTBOUND_TRANSFER_STATION_OBJECTIVE, OUTBOUND_TRANSFER_STATION_TITLE, 0);
        setPlayerScore(world, player, OUTBOUND_TRANSFER_TIME_OBJECTIVE, OUTBOUND_TRANSFER_TIME_TITLE, 0);
        player.sendMessage(Text.cast(TextHelper.translatable("gui,tjmetro.outbound_transfer_success")), true);
        cir.setReturnValue(true);
    }
}
