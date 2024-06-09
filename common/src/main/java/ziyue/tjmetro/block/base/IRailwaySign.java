package ziyue.tjmetro.block.base;

import mtr.block.BlockRailwaySign;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.CustomResources;
import mtr.data.IGui;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ziyue.tjmetro.Reference;
import ziyue.tjmetro.client.ClientCache;

import java.util.Arrays;
import java.util.List;

import static mtr.client.CustomResources.CUSTOM_SIGNS;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

/**
 * Methods that requires specific "Middle Block" of Railway Sign.
 *
 * @author ZiYueCommentary
 * @see BlockRailwaySignBase
 * @since beta-1
 */

public interface IRailwaySign
{
    /**
     * @see mtr.block.BlockRailwaySign#setPlacedBy(Level, BlockPos, BlockState, LivingEntity, ItemStack)
     */
    static void setPlacedBy(Level world, BlockPos pos, BlockState state, Block middle, int middleLength) {
        if (world.isClientSide) return;
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        for (int i = 1; i <= middleLength; i++) {
            world.setBlock(pos.relative(facing.getClockWise(), i), middle.defaultBlockState().setValue(FACING, facing), 3);
        }
        world.setBlock(pos.relative(facing.getClockWise(), middleLength + 1), state.getBlock().defaultBlockState().setValue(FACING, facing.getOpposite()), 3);
        world.updateNeighborsAt(pos, Blocks.AIR);
        state.updateNeighbourShapes(world, pos, 3);
    }

    /**
     * @see mtr.block.BlockRailwaySign#updateShape(BlockState, Direction, BlockState, LevelAccessor, BlockPos, BlockPos)
     */
    static BlockState updateShape(BlockState state, Direction direction, BlockState newState, Block middle) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final boolean isNext = (direction == facing.getClockWise()) || state.is(middle) && (direction == facing.getCounterClockWise());
        if (isNext && !(newState.getBlock() instanceof BlockRailwaySignBase)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    /**
     * @see mtr.block.BlockRailwaySign#getShape(BlockState, BlockGetter, BlockPos, CollisionContext)
     */
    static VoxelShape getShape(BlockState state, int xStart, Block middle) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (state.is(middle)) {
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
        } else {
            final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 0.75, 0, 7, 16, 12, 9, facing);
            final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7, xStart - 0.75, 16, 9, facing);
            return Shapes.or(main, pole);
        }
    }

    static BlockPos findEndWithDirection(Level world, BlockPos startPos, Direction direction, boolean allowOpposite, Block middle) {
        int i = 0;
        while (true) {
            final BlockPos checkPos = startPos.relative(direction.getCounterClockWise(), i);
            final BlockState checkState = world.getBlockState(checkPos);
            if (checkState.getBlock() instanceof BlockRailwaySignBase) {
                final Direction facing = IBlock.getStatePropertySafe(checkState, FACING);
                if (!checkState.is(middle) && (facing == direction || allowOpposite && facing == direction.getOpposite())) {
                    return checkPos;
                }
            } else {
                return null;
            }
            i++;
        }
    }

    static boolean signIsExit(String signId) {
        List<String> exits = Arrays.asList(BlockRailwaySign.SignType.EXIT_LETTER.toString(), BlockRailwaySign.SignType.EXIT_LETTER_FLIPPED.toString(),
                SignType.EXIT_LETTER_TEXT.signId, SignType.EXIT_LETTER_TEXT_FLIPPED.signId);
        return exits.contains(signId);
    }

    static boolean signIsLine(String signId) {
        List<String> lines = Arrays.asList(BlockRailwaySign.SignType.LINE.toString(), BlockRailwaySign.SignType.LINE_FLIPPED.toString());
        return lines.contains(signId);
    }

    static boolean signIsPlatform(String signId) {
        List<String> platforms = Arrays.asList(BlockRailwaySign.SignType.PLATFORM.toString(), BlockRailwaySign.SignType.PLATFORM_FLIPPED.toString(),
                SignType.BOUND_FOR_TEXT.signId, SignType.BOUND_FOR_TEXT_FLIPPED.signId);
        return platforms.contains(signId);
    }

    static boolean signIsStation(String signId) {
        List<String> stations = Arrays.asList(BlockRailwaySign.SignType.STATION.toString(), BlockRailwaySign.SignType.STATION_FLIPPED.toString(),
                SignType.STATION_TEXT.signId, SignType.STATION_TEXT_FLIPPED.signId);
        return stations.contains(signId);
    }

    static ResourceLocation getExitSignResource(String signId, String exitLetter, String exitNumber, int backgroundColor, boolean forceMTRFont) {
        if (signId.equals(SignType.EXIT_LETTER_TEXT.signId) || signId.equals(SignType.EXIT_LETTER_TEXT_FLIPPED.signId)) {
            return ClientCache.DATA_CACHE.getExitSignLetterTianjin(exitLetter, exitNumber, backgroundColor, forceMTRFont).resourceLocation;
        } else {
            if (forceMTRFont) {
                return ClientData.DATA_CACHE.getExitSignLetter(exitLetter, exitNumber, backgroundColor).resourceLocation;
            } else {
                return ClientCache.DATA_CACHE.getExitSignLetter(exitLetter, exitNumber, backgroundColor).resourceLocation;
            }
        }
    }

    static ResourceLocation getPlatformSignResource(String signId, long platformId, IGui.HorizontalAlignment horizontalAlignment, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor, boolean forceMTRFont) {
        if (signId.equals(SignType.BOUND_FOR_TEXT.signId) || signId.equals(SignType.BOUND_FOR_TEXT_FLIPPED.signId)) {
            return ClientCache.DATA_CACHE.getBoundFor(platformId, horizontalAlignment, aspectRatio, paddingScale, backgroundColor, forceMTRFont).resourceLocation;
        } else {
            if (forceMTRFont) {
                return ClientData.DATA_CACHE.getDirectionArrow(platformId, false, false, horizontalAlignment, false, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor).resourceLocation;
            } else {
                return ClientCache.DATA_CACHE.getDirectionArrow(platformId, false, false, horizontalAlignment, false, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor).resourceLocation;
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
        public final CustomResources.CustomSign sign;

        SignType(String texture, String translation, boolean flipTexture, boolean flipCustomText, boolean hasCustomText, int backgroundColor) {
            this.signId = (this.toString().contains("BMT") ? "\1_TJMETRO_%s" : "\0_TJMETRO_%s").formatted(this.toString()); // Make sure that signs will always order in front of custom signs, and BMT signs are after TRT ;)
            this.sign = new CustomResources.CustomSign(new ResourceLocation(Reference.MOD_ID, "textures/sign/" + texture + ".png"), flipTexture, hasCustomText ? Text.translatable("sign.tjmetro." + translation + "_cjk").append("|").append(Text.translatable("sign.tjmetro." + translation)).getString() : "", flipCustomText, true, backgroundColor);
            CUSTOM_SIGNS.put(signId, sign);
        }

        SignType(String texture, String translation, boolean flipTexture, boolean flipCustomText) {
            this(texture, translation, flipTexture, flipCustomText, true, 0);
        }

        SignType(String texture, boolean flipTexture) {
            this(texture, texture, flipTexture, false, false, 0);
        }
    }
}
