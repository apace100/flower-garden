package io.github.apace100.flowerfix.mixin;

import io.github.apace100.flowerfix.FlowerGarden;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockItem.class)
public abstract class RandomMixin {

    @Shadow public abstract Block getBlock();

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemPlacementContext;getPlayer()Lnet/minecraft/entity/player/PlayerEntity;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void randomizeOffset(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir, ItemPlacementContext itemPlacementContext, BlockState state, BlockPos pos, World world) {
        if(!world.isClient) {
            Block block = getBlock() ;
            if(block instanceof FlowerBlock) {
                float offsetH = block.getMaxModelOffset();
                float offsetV = block.method_37247();
                AbstractBlock.OffsetType offsetType = block.getOffsetType();
                WorldChunk chunk = world.getWorldChunk(pos);
                FlowerGarden.FLOWER_OFFSETS.maybeGet(chunk).ifPresent(bsps -> bsps.randomize(pos, offsetType, offsetH, offsetV));
                FlowerGarden.FLOWER_OFFSETS.sync(chunk);
            }
        }
    }
}
