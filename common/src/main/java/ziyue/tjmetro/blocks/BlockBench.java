package ziyue.tjmetro.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@SuppressWarnings("developing")
public class BlockBench extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public BlockBench() {
        super(Properties.copy(Blocks.OAK_PLANKS));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        Entity seat = new Minecart(level, blockPos.getX(), blockPos.getY(), blockPos.getZ());

        return InteractionResult.SUCCESS;
    }
}
