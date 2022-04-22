package io.github.apace100.flowerfix.component;

import io.github.apace100.flowerfix.FlowerGarden;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtTypes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class BlockSubPosStorageImpl implements BlockSubPosStorage {

    private final Random random = new Random();
    private final Chunk chunk;
    private HashMap<BlockPos, Vec3d> subPositions = new HashMap<>();

    public BlockSubPosStorageImpl(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public Optional<Vec3d> getSubPosition(BlockPos pos) {
        if(subPositions.containsKey(pos)) {
            return Optional.of(subPositions.get(pos));
        }
        return Optional.empty();
    }

    @Override
    public void setSubPosition(BlockPos pos, Vec3d subPosition) {
        subPositions.put(pos, subPosition);
        chunk.setShouldSave(true);
    }

    @Override
    public void removeSubPosition(BlockPos pos) {
        subPositions.remove(pos);
        chunk.setShouldSave(true);
    }

    @Override
    public void randomize(BlockPos pos, AbstractBlock.OffsetType offsetType, float maxOffsetHorizontal, float maxOffsetVertical) {
        double d = random.nextDouble() * maxOffsetHorizontal * 2 - maxOffsetHorizontal;
        double e = offsetType == AbstractBlock.OffsetType.XYZ ? random.nextDouble() * maxOffsetVertical : 0.0D;
        double g = random.nextDouble() * maxOffsetHorizontal * 2 - maxOffsetHorizontal;
        setSubPosition(pos, new Vec3d(d, e, g));
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        subPositions = getSubPositionsFromTag(tag);
    }

    private HashMap<BlockPos, Vec3d> getSubPositionsFromTag(NbtCompound tag) {
        HashMap<BlockPos, Vec3d> map = new HashMap<>();
        if(tag.contains("SubPositions", NbtType.LIST)) {
            NbtList list = (NbtList)tag.get("SubPositions");
            list.forEach(nbtElement -> {
                if(nbtElement instanceof NbtCompound) {
                    NbtCompound entryNbt = (NbtCompound) nbtElement;
                    BlockPos pos = BlockPos.fromLong(entryNbt.getLong("Position"));
                    double x = entryNbt.getDouble("X");
                    double y = entryNbt.getDouble("Y");
                    double z = entryNbt.getDouble("Z");
                    Vec3d subPosition = new Vec3d(x, y, z);
                    map.put(pos, subPosition);
                }
            });
        }
        return map;
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList list = new NbtList();
        for(Map.Entry<BlockPos, Vec3d> entry : subPositions.entrySet()) {
            NbtCompound entryNbt = new NbtCompound();
            entryNbt.putLong("Position", entry.getKey().asLong());
            entryNbt.putDouble("X", entry.getValue().x);
            entryNbt.putDouble("Y", entry.getValue().y);
            entryNbt.putDouble("Z", entry.getValue().z);
            list.add(entryNbt);
        }
        tag.put("SubPositions", list);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void applySyncPacket(PacketByteBuf buf) {
        NbtCompound tag = buf.readNbt();
        if(tag != null) {
            HashMap<BlockPos, Vec3d> updatePositions = getSubPositionsFromTag(tag);
            MinecraftClient client = MinecraftClient.getInstance();
            if(client != null) {
                WorldRenderer worldRenderer = client.worldRenderer;
                if(worldRenderer != null) {
                    for(BlockPos pos : subPositions.keySet()) {
                        if(!updatePositions.containsKey(pos)) {
                            worldRenderer.scheduleBlockRenders(
                                pos.getX(), pos.getY(), pos.getZ(),
                                pos.getX(), pos.getY(), pos.getZ()
                            );
                        }
                    }
                    for(Map.Entry<BlockPos, Vec3d> entry : updatePositions.entrySet()) {
                        if(!subPositions.containsKey(entry.getKey()) || !subPositions.get(entry.getKey()).equals(entry.getValue())) {
                            BlockPos pos = entry.getKey();
                            worldRenderer.scheduleBlockRenders(
                                pos.getX(), pos.getY(), pos.getZ(),
                                pos.getX(), pos.getY(), pos.getZ()
                            );
                        }
                    }
                }
            }
            subPositions = updatePositions;
        }
    }
}
