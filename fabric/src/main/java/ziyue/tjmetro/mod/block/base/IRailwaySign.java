package ziyue.tjmetro.mod.block.base;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.data.IGui;
import org.mtr.mod.resource.SignResource;
import ziyue.tjmetro.mod.Reference;
import ziyue.tjmetro.mod.block.IBlockExtension;
import ziyue.tjmetro.mod.client.DynamicTextureCache;
import ziyue.tjmetro.mod.data.IGuiExtension;

import java.util.Arrays;
import java.util.List;

public interface IRailwaySign extends DirectionHelper
{
    static void onPlaced(World world, BlockPos pos, BlockState state, Block middle, int middleLength) {
        if (world.isClient()) return;

        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        for (int i = 1; i <= middleLength; i++) {
            world.setBlockState(pos.offset(facing.rotateYClockwise(), i), middle.getDefaultState().with(new Property<>(FACING.data), facing.data), 3);
        }
        world.setBlockState(pos.offset(facing.rotateYClockwise(), middleLength + 1), state.getBlock().getDefaultState().with(new Property<>(FACING.data), facing.getOpposite().data), 3);
        world.updateNeighbors(pos, Blocks.getAirMapped());
        state.updateNeighbors(new WorldAccess(world.data), pos, 3);
    }

    static BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, Block middle) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final boolean isNext = (direction == facing.rotateYClockwise()) || IBlockExtension.isBlock(state, middle) && (direction == facing.rotateYCounterclockwise());
        if (isNext && !(newState.getBlock().data instanceof BlockRailwaySignBase)) {
            return Blocks.getAirMapped().getDefaultState();
        } else {
            return state;
        }
    }

    static VoxelShape getOutlineShape(BlockState state, int xStart, Block middle) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (IBlockExtension.isBlock(state, middle)) {
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
        } else {
            final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 0.75, 0, 7, 16, 12, 9, facing);
            final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7, xStart - 0.75, 16, 9, facing);
            return VoxelShapes.union(main, pole);
        }
    }

    static BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite, Block middle) {
        int i = 0;
        while (true) {
            final BlockPos checkPos = startPos.offset(direction.rotateYCounterclockwise(), i);
            final BlockState checkState = world.getBlockState(checkPos);
            if (checkState.getBlock().data instanceof BlockRailwaySignBase) {
                final Direction facing = IBlock.getStatePropertySafe(checkState, FACING);
                if (!IBlockExtension.isBlock(checkState, middle) && (facing == direction || allowOpposite && facing == direction.getOpposite())) {
                    return checkPos;
                }
            } else {
                return null;
            }
            i++;
        }
    }

    static boolean signIsExit(String signId) {
        List<String> exits = Arrays.asList("exit_letter", "exit_letter_flipped",
                SignType.EXIT_LETTER_TEXT.signId, SignType.EXIT_LETTER_TEXT_FLIPPED.signId);
        return exits.contains(signId);
    }

    static boolean signIsLine(String signId) {
        List<String> lines = Arrays.asList("line", "line_flipped");
        return lines.contains(signId);
    }

    static boolean signIsPlatform(String signId) {
        List<String> platforms = Arrays.asList("platform", "platform_flipped",
                SignType.BOUND_FOR_TEXT.signId, SignType.BOUND_FOR_TEXT_FLIPPED.signId);
        return platforms.contains(signId);
    }

    static boolean signIsStation(String signId) {
        List<String> stations = Arrays.asList("station", "station_flipped",
                SignType.STATION_TEXT.signId, SignType.STATION_TEXT_FLIPPED.signId);
        return stations.contains(signId);
    }

    static Identifier getExitSignResource(String signId, String exitLetter, String exitNumber, int backgroundColor, boolean forceMTRFont) {
        if (signId.equals(SignType.EXIT_LETTER_TEXT.signId) || signId.equals(SignType.EXIT_LETTER_TEXT_FLIPPED.signId)) {
            return DynamicTextureCache.instance.getExitSignLetterTianjin(exitLetter, exitNumber, backgroundColor, forceMTRFont).identifier;
        } else {
            if (forceMTRFont) {
                return org.mtr.mod.client.DynamicTextureCache.instance.getExitSignLetter(exitLetter, exitNumber, backgroundColor).identifier;
            } else {
                return DynamicTextureCache.instance.getExitSignLetter(exitLetter, exitNumber, backgroundColor).identifier;
            }
        }
    }

    static Identifier getPlatformSignResource(String signId, long platformId, IGui.HorizontalAlignment horizontalAlignment, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor, boolean forceMTRFont) {
        if (signId.equals(SignType.BOUND_FOR_TEXT.signId) || signId.equals(SignType.BOUND_FOR_TEXT_FLIPPED.signId)) {
            return DynamicTextureCache.instance.getBoundFor(platformId, horizontalAlignment, aspectRatio, paddingScale, backgroundColor, forceMTRFont).identifier;
        } else {
            if (forceMTRFont) {
                return org.mtr.mod.client.DynamicTextureCache.instance.getDirectionArrow(platformId, false, false, horizontalAlignment, false, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor).identifier;
            } else {
                return DynamicTextureCache.instance.getDirectionArrow(platformId, false, false, horizontalAlignment, false, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor).identifier;
            }
        }
    }

    enum SignType
    {
        // Tianjin Rail Transit (TRT, Tianjin Metro)
        TIANJIN_METRO_LOGO_TEXT("tianjin_metro_logo", "tianjin_metro", false, false),
        TIANJIN_METRO_LOGO_TEXT_FLIPPED("tianjin_metro_logo", "tianjin_metro", false, true),
        TIANJIN_METRO_MOD_LOGO_TEXT("tianjin_metro_mod_logo", "tianjin_metro", false, false),
        TIANJIN_METRO_MOD_LOGO_TEXT_FLIPPED("tianjin_metro_mod_logo", "tianjin_metro", false, true),
        TIANJIN_METRO_OLD_LOGO_TEXT("tianjin_metro_old_logo", "tianjin_metro", false, false),
        TIANJIN_METRO_OLD_LOGO_TEXT_FLIPPED("tianjin_metro_old_logo", "tianjin_metro", false, true),
        TRAIN_TEXT("train", "train", false, false),
        TRAIN_TEXT_FLIPPED("train", "train", false, true),
        STATION_TEXT("train", "station", false, false),
        STATION_TEXT_FLIPPED("train", "station", false, true),
        EMERGENCY_EXIT_TEXT("emergency_exit", "emergency_exit", false, false),
        EMERGENCY_EXIT_TEXT_FLIPPED("emergency_exit", "emergency_exit", true, true),
        NO_THROUGHFARE_TEXT("no_throughfare", "no_throughfare", false, false),
        NO_THROUGHFARE_TEXT_FLIPPED("no_throughfare", "no_throughfare", false, true),
        SECURITY_CHECK_TEXT("security_check", "security_check", false, false),
        SECURITY_CHECK_TEXT_FLIPPED("security_check", "security_check", false, true),
        AUTOMATIC_TICKET_TEXT("automatic_ticket", "automatic_ticket", false, false),
        AUTOMATIC_TICKET_TEXT_FLIPPED("automatic_ticket", "automatic_ticket", false, true),
        RAILWAY_STATION_TEXT("railway_station", "railway_station", false, false),
        RAILWAY_STATION_TEXT_FLIPPED("railway_station", "railway_station", false, true),
        ACCESSIBLE_ELEVATOR_TEXT("accessible_elevator", "accessible_elevator", false, false),
        ACCESSIBLE_ELEVATOR_TEXT_FLIPPED("accessible_elevator", "accessible_elevator", false, true),
        ACCESSIBLE_TOILET_TEXT("accessible_toilet", "accessible_toilet", false, false),
        ACCESSIBLE_TOILET_TEXT_FLIPPED("accessible_toilet", "accessible_toilet", false, true),
        FARE_ADJUSTMENT_TEXT("fare_adjustment", "fare_adjustment", false, false),
        FARE_ADJUSTMENT_TEXT_FLIPPED("fare_adjustment", "fare_adjustment", false, true),
        INQUIRY_TEXT("inquiry", "inquiry", false, false),
        INQUIRY_TEXT_FLIPPED("inquiry", "inquiry", false, true),
        EXIT_LETTER_TEXT("exit_letter", "exit_bmt", false, false),
        EXIT_LETTER_TEXT_FLIPPED("exit_letter", "exit_bmt", false, true),
        BOUND_FOR_TEXT("bound_for", "bound_for", false, false),
        BOUND_FOR_TEXT_FLIPPED("bound_for", "bound_for", false, true),
        ACCESSIBLE_PASSAGE_TEXT("accessible_passage", "accessible_passage", false, false),
        ACCESSIBLE_PASSAGE_TEXT_FLIPPED("accessible_passage", "accessible_passage", true, true),
        TOILET_TEXT("toilet", "toilet", false, false),
        TOILET_TEXT_FLIPPED("toilet", "toilet", false, true),

        TIANJIN_METRO_LOGO("tianjin_metro_logo", false),
        TIANJIN_METRO_MOD_LOGO("tianjin_metro_mod_logo", false),
        TIANJIN_METRO_OLD_LOGO("tianjin_metro_old_logo", false),
        TRAIN("train", false),
        EMERGENCY_EXIT("emergency_exit", false),
        EMERGENCY_EXIT_FLIPPED("emergency_exit", true),
        NO_THROUGHFARE("no_throughfare", false),
        SECURITY_CHECK("security_check", false),
        AUTOMATIC_TICKET("automatic_ticket", false),
        RAILWAY_STATION("railway_station", false),
        ACCESSIBLE_ELEVATOR("accessible_elevator", false),
        ACCESSIBLE_TOILET("accessible_toilet", false),
        FARE_ADJUSTMENT("fare_adjustment", false),
        INQUIRY("inquiry", false),
        //EXIT("exit", false),
        STAIRS("stairs", false),
        STAIRS_FLIPPED("stairs", true),
        ACCESSIBLE_PASSAGE("accessible_passage", false),
        ACCESSIBLE_PASSAGE_FLIPPED("accessible_passage", true),
        ESCALATOR("escalator", false),
        ESCALATOR_FLIPPED("escalator", true),
        TOILET("toilet", false),

        // Tianjin Binhai Mass Transit (BMT, Tianjin Metro Line 9)
        NO_ENTRY_BMT_TEXT("no_entry_bmt", "no_entry_bmt", false, false),
        NO_ENTRY_BMT_TEXT_FLIPPED("no_entry_bmt", "no_entry_bmt", false, true),
        TO_SUBWAY_BMT_TEXT("to_subway_bmt", "to_subway_bmt", false, false),
        TO_SUBWAY_BMT_TEXT_FLIPPED("to_subway_bmt", "to_subway_bmt", true, true),
        EXIT_BMT_UP_TEXT("exit_bmt_up", "exit_bmt", false, false),
        EXIT_BMT_DOWN_TEXT("exit_bmt_down", "exit_bmt", false, false),
        EXIT_BMT_UP_TEXT_FLIPPED("exit_bmt_up", "exit_bmt", false, true),
        EXIT_BMT_DOWN_TEXT_FLIPPED("exit_bmt_down", "exit_bmt", false, true),
        EXIT_BMT_LEFT_TEXT("exit_bmt_left", "exit_bmt", false, false),
        EXIT_BMT_RIGHT_TEXT("exit_bmt_right", "exit_bmt", false, true),
        TICKETS_BMT_TEXT("fare_adjustment", "tickets_bmt", false, false),
        TICKETS_BMT_TEXT_FLIPPED("fare_adjustment", "tickets_bmt", false, true),

        NO_ENTRY_BMT("no_entry_bmt", false),
        TO_SUBWAY_BMT("to_subway_bmt", false),
        TO_SUBWAY_BMT_FLIPPED("to_subway_bmt", true),
        //BINHAI_MASS_TRANSIT("binhai_mass_transit", false),
        //BINHAI_MASS_TRANSIT_FLIPPED("binhai_mass_transit", true),
        EXIT_BMT_UP("exit_bmt_up", false),
        EXIT_BMT_DOWN("exit_bmt_down", false),
        EXIT_BMT_LEFT("exit_bmt_left", false),
        EXIT_BMT_RIGHT("exit_bmt_right", false);

        public final String signId;
        public final SignResource sign;

        SignType(String texture, String translation, boolean flipTexture, boolean flipCustomText, boolean hasCustomText, int backgroundColor) {
            this.signId = (this.toString().contains("BMT") ? "\1_TJMETRO_%s" : "\0_TJMETRO_%s").formatted(this.toString()).toLowerCase(); // Make sure that signs will always order in front of custom signs, and BMT signs are after TRT ;)
            final JsonObject object = new JsonObject();
            object.addProperty("id", this.signId);
            object.addProperty("textureResource", Reference.MOD_ID + ":textures/sign/" + texture + ".png");
            object.addProperty("flipTexture", flipTexture);
            object.addProperty("customText", hasCustomText ? IGuiExtension.mergeTranslation("sign.tjmetro." + translation + "_cjk", "sign.tjmetro." + translation) : "");
            object.addProperty("flipCustomText", flipCustomText);
            object.addProperty("small", true);
            object.addProperty("backgroundColor", backgroundColor);
            this.sign = new SignResource(new JsonReader(object));
        }

        SignType(String texture, String translation, boolean flipTexture, boolean flipCustomText) {
            this(texture, translation, flipTexture, flipCustomText, true, 0);
        }

        SignType(String texture, boolean flipTexture) {
            this(texture, texture, flipTexture, false, false, 0);
        }
    }
}
