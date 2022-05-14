package ziyue.tjmetro;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;
import static ziyue.tjmetro.Main.LOGGER;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public interface IBlockExtension
{
    static void onBreakCreative(Level world, Player player, BlockPos pos) {
        if (!world.isClientSide && player == null || player.isCreative()) {
            try {
                world.setBlock(pos, world.getBlockState(pos).getValue(WATERLOGGED) ? net.minecraft.world.level.block.Blocks.WATER.defaultBlockState() : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            } catch(Exception exception) {
                LOGGER.error("Get " + WATERLOGGED.getName() + " status failed: replace with air");
                world.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            }
            final BlockState state = world.getBlockState(pos);
            world.levelEvent(player, 2001, pos, Block.getId(state));
        }
    }

    static void onBreakCreative(Level world, Player player, BlockPos pos, Block block) {
        if (!world.isClientSide && player == null || player.isCreative()) {
            try {
                if(world.getBlockState(pos).getBlock() == block)
                    world.setBlock(pos, world.getBlockState(pos).getValue(WATERLOGGED) ? net.minecraft.world.level.block.Blocks.WATER.defaultBlockState() : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            } catch(Exception exception) {
                LOGGER.error("Get " + WATERLOGGED.getName() + " status failed: replace with air");
                world.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            }
            final BlockState state = world.getBlockState(pos);
            world.levelEvent(player, 2001, pos, Block.getId(state));
        }
    }

    static void onBreakCreative(Level world, Player player, BlockPos pos, BooleanProperty property) {
        if (!world.isClientSide && player == null || player.isCreative()) {
            try {
                world.setBlock(pos, world.getBlockState(pos).getValue(property) ? net.minecraft.world.level.block.Blocks.WATER.defaultBlockState() : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            } catch(Exception exception) {
                LOGGER.error("Get " + property.getName() + " status failed: replace with air");
                world.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            }
            final BlockState state = world.getBlockState(pos);
            world.levelEvent(player, 2001, pos, Block.getId(state));
        }
    }

    static void onBreakCreative(Level world, Player player, BlockPos pos, Block block, BooleanProperty property) {
        if (!world.isClientSide && player == null || player.isCreative()) {
            try {
                world.setBlock(pos, world.getBlockState(pos).getValue(property) ? block.defaultBlockState() : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            } catch(Exception exception) {
                LOGGER.error("Get " + property.getName() + " status failed: replace with air");
                world.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            }
            final BlockState state = world.getBlockState(pos);
            world.levelEvent(player, 2001, pos, Block.getId(state));
        }
    }

    static void onBreakCreative(Level world, Player player, BlockPos pos, Block block, DirectionProperty property, Direction direction) {
        if (!world.isClientSide && player == null || player.isCreative()) {
            try {
                world.setBlock(pos, world.getBlockState(pos).getValue(property) == direction ? block.defaultBlockState() : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            } catch(Exception exception) {
                LOGGER.error("Get " + property.getName() + " status failed: replace with air");
                world.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            }
            final BlockState state = world.getBlockState(pos);
            world.levelEvent(player, 2001, pos, Block.getId(state));
        }
    }
}
