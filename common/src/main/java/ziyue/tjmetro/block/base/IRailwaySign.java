package ziyue.tjmetro.block.base;

import mtr.block.IBlock;
import mtr.client.CustomResources;
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

        // Tianjin Binhai Mass Transit (BMT, Tianjin Metro Line 9)
        NO_ENTRY_BMT_TEXT("no_entry_bmt", "no_entry_bmt", false, false),
        NO_ENTRY_BMT_TEXT_FLIPPED("no_entry_bmt", "no_entry_bmt", false, true),
        TO_SUBWAY_BMT_TEXT("to_subway_bmt", "to_subway_bmt", false, false),
        TO_SUBWAY_BMT_TEXT_FLIPPED("to_subway_bmt", "to_subway_bmt", true, true),

        NO_ENTRY_BMT("no_entry_bmt", false),
        TO_SUBWAY_BMT("to_subway_bmt", false),
        TO_SUBWAY_BMT_FLIPPED("to_subway_bmt", true);
        //BINHAI_MASS_TRANSIT("binhai_mass_transit", false),
        //BINHAI_MASS_TRANSIT_FLIPPED("binhai_mass_transit", true);

        public final String signId;
        public final CustomResources.CustomSign sign;

        SignType(String texture, String translation, boolean flipTexture, boolean flipCustomText, boolean hasCustomText, int backgroundColor) {
            this.signId = (texture.endsWith("bmt") ? "\1tjmetro_%s_%s_%s_%s_%s_%s" : "\0tjmetro_%s_%s_%s_%s_%s_%s") // Make sure that signs will always order in front of custom signs, and BMT signs are after TRT ;)
                    .formatted(texture, translation, flipTexture, flipCustomText, hasCustomText, backgroundColor);
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
