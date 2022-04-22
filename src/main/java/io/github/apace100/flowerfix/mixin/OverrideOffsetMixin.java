package io.github.apace100.flowerfix.mixin;

import io.github.apace100.flowerfix.FlowerGarden;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class OverrideOffsetMixin {

    @Inject(method = "getModelOffset", at = @At("HEAD"), cancellable = true)
    private void overrideModelOffset(BlockView world, BlockPos pos, CallbackInfoReturnable<Vec3d> cir) {
        AbstractBlock.AbstractBlockState state = (AbstractBlock.AbstractBlockState) (Object) this;
        Optional<Vec3d> override = FlowerGarden.getOverriddenModelOffset(state, world, pos);
        override.ifPresent(cir::setReturnValue);
    }
}
