package io.github.apace100.flowerfix.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public interface BlockSubPosStorage extends AutoSyncedComponent {

    Optional<Vec3d> getSubPosition(BlockPos pos);
    void setSubPosition(BlockPos pos, Vec3d subPosition);
    void removeSubPosition(BlockPos pos);
    default void randomize(BlockPos pos, AbstractBlock.OffsetType offsetType, float maxOffset) {
        randomize(pos, offsetType, maxOffset, 0f);
    }
    void randomize(BlockPos pos, AbstractBlock.OffsetType offsetType, float maxOffsetHorizontal, float maxOffsetVertical);
}
