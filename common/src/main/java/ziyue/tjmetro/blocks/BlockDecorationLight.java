package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.blocks.base.BlockCustomColorBase;

//todo so I must make this because this placeholder has existed for half year
public class BlockDecorationLight extends BlockCustomColorBase
{
    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("level", 1, 15);

    public BlockDecorationLight() {
        this(BlockBehaviour.Properties.copy(Blocks.CEILING_LIGHT.get()).noCollission());
    }

    public BlockDecorationLight(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        blockState.cycle(LIGHT_LEVEL);
        return InteractionResult.SUCCESS;
    }

    @Override
    public int getLightBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getValue(LIGHT_LEVEL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new DecorationLightEntity(pos, state);
    }

    public static class DecorationLightEntity extends CustomColorBlockEntity
    {
        public DecorationLightEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.DECORATION_LIGHT_TILE_ENTITY.get(), pos, state);
        }
    }
}
