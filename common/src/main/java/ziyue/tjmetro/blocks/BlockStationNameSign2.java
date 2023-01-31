package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.blocks.base.BlockStationNameSignBase;

/**
 * Second variant for <b>Station Name Sign</b>.
 *
 * @author ZiYueCommentary
 * @see BlockStationNameSignBase
 * @since beta-1
 */

public class BlockStationNameSign2 extends BlockStationNameSignBase
{
    public BlockStationNameSign2() {
        this(BlockBehaviour.Properties.copy(Blocks.STATION_NAME_TALL_WALL.get()));
    }

    public BlockStationNameSign2(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getName().toString().equals("EnderkingIIII")) { //easter egg lol
            world.explode(player, DamageSource.MAGIC, new ExplosionDamageCalculator(), player.getX(), player.getY(), player.getZ(), 5f, true, Explosion.BlockInteraction.DESTROY);
            LOGGER.warn("EnderkingIIII found!");
        }
        return super.use(state, world, pos, player, interactionHand, blockHitResult);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockStationNameSign2.TileEntityStationNameWall(pos, state);
    }

    public static class TileEntityStationNameWall extends BlockStationNameSignBase.TileEntityStationNameBase
    {
        public TileEntityStationNameWall(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_SIGN_ENTITY_2.get(), pos, state);
        }
    }
}
