package io.github.apace100.flowerfix;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.github.apace100.flowerfix.component.BlockSubPosStorage;
import io.github.apace100.flowerfix.component.BlockSubPosStorageImpl;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.BiFunction;

public class FlowerGarden implements ModInitializer, ChunkComponentInitializer {

	public static final String MODID = "flower-garden";
	public static final Logger LOGGER = LogManager.getLogger(FlowerGarden.class);

	public static final ComponentKey<BlockSubPosStorage> FLOWER_OFFSETS
		= ComponentRegistry.getOrCreate(new Identifier(MODID, "flower_offsets"), BlockSubPosStorage.class);

	static BiFunction<BlockView, BlockPos, Object> chunkProviderGetter;

	@Override
	public void onInitialize() {
	}

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(FLOWER_OFFSETS, BlockSubPosStorageImpl::new);
	}

	public static Vec3d getModelOffset(AbstractBlock.AbstractBlockState state, BlockView blockView, BlockPos pos) {
		return getOverriddenModelOffset(state, blockView, pos).orElse(state.getModelOffset(blockView, pos));
	}

	public static Optional<Vec3d> getOverriddenModelOffset(AbstractBlock.AbstractBlockState state, BlockView blockView, BlockPos pos) {
		Optional<BlockSubPosStorage> flowerOffsetStorageOptional =
			FLOWER_OFFSETS.maybeGet(chunkProviderGetter.apply(blockView, pos));
		if(flowerOffsetStorageOptional.isEmpty()) {
			return Optional.empty();
		} else {
			Optional<Vec3d> override = flowerOffsetStorageOptional.get().getSubPosition(pos);
			return override;
		}
	}
}
