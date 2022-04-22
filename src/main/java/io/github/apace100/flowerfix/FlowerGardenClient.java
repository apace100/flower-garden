package io.github.apace100.flowerfix;

import io.github.apace100.flowerfix.mixin.ChunkRendererRegionAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

@Environment(EnvType.CLIENT)
public class FlowerGardenClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		FlowerGarden.chunkProviderGetter = ((blockView, pos) -> {
			if(blockView instanceof Chunk) {
				return blockView;
			} else if(blockView instanceof World w) {
				return w.getWorldChunk(pos);
			} else if(blockView instanceof ChunkRendererRegion crr) {
				return ((ChunkRendererRegionAccessor)crr).getWorld().getWorldChunk(pos);
			}
			return blockView;
		});
	}
}
