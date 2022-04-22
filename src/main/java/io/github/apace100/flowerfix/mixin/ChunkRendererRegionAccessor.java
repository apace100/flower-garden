package io.github.apace100.flowerfix.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkRendererRegion.class)
@Environment(EnvType.CLIENT)
public interface ChunkRendererRegionAccessor {

    @Accessor
    World getWorld();
}
