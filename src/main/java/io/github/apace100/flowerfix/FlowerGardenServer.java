package io.github.apace100.flowerfix;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class FlowerGardenServer implements DedicatedServerModInitializer {

	@Override
	public void onInitializeServer() {
		FlowerGarden.chunkProviderGetter = ((blockView, pos) -> {
			if(blockView instanceof Chunk) {
				return blockView;
			} else if(blockView instanceof World w) {
				return w.getWorldChunk(pos);
			}
			return blockView;
		});
	}
}
