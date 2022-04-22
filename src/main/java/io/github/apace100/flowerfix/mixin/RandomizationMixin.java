package io.github.apace100.flowerfix.mixin;

import io.github.apace100.flowerfix.FlowerGarden;
import io.github.apace100.flowerfix.component.BlockSubPosStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(FlowerBlock.class)
public class RandomizationMixin extends PlantBlock {

    protected RandomizationMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        BlockView chunk = world.getChunkManager().getChunk(
            ChunkSectionPos.getSectionCoord(pos.getX()),
            ChunkSectionPos.getSectionCoord(pos.getZ()));
        FlowerGarden.FLOWER_OFFSETS.maybeGet(chunk).ifPresent(bsps -> bsps.removeSubPosition(pos));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);/*
        if(!world.isClient) {
            Block block = state.getBlock();
            float offsetH = block.getMaxModelOffset();
            float offsetV = block.method_37247();
            OffsetType offsetType = block.getOffsetType();
            WorldChunk chunk = world.getWorldChunk(pos);
            FlowerGarden.FLOWER_OFFSETS.maybeGet(chunk).ifPresent(bsps -> bsps.randomize(pos, offsetType, offsetH, offsetV));
            FlowerGarden.FLOWER_OFFSETS.sync(chunk);
        }*/
    }
}
