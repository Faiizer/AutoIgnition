package net.autoignition.inventory;

import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.StateData;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState;
import net.autoignition.cache.BenchCache;
import net.autoignition.util.AutoIgnitionBlockUtil;
import net.autoignition.util.AutoIgnitionChunkUtil;
import net.autoignition.util.AutoIgnitionMathUtil;

/**
 * Provides spatial scanning logic to identify neighboring storage containers.
 */
public class NeighborScanner {

    /** Cardinal horizontal offsets for neighbor checking */
    private static final Vector3i[] HORIZONTAL_OFFSETS = {
            new Vector3i(1, 0, 0),
            new Vector3i(-1, 0, 0),
            new Vector3i(0, 0, 1),
            new Vector3i(0, 0, -1)
    };

    /**
     * Entry point for scanning containers around a specific bench.
     * It clears the existing cache and performs a fresh scan of all relevant blocks.
     * @param world The world instance.
     * @param position The master block position of the bench.
     * @param cache The cache to store discovered container positions.
     * @param bench The state of the bench performing the scan.
     */
    @SuppressWarnings("removal")
    public static void scan(World world, Vector3i position, BenchCache cache, ProcessingBenchState bench) {
        cache.updateScanTime();
        cache.getContainerPositions().clear();

        scanAt(world, position, cache);

        if (AutoIgnitionBlockUtil.isMultiBlock(bench)) {
            Vector3i offset = AutoIgnitionMathUtil.getSecondBenchBlockOffset(bench.getRotationIndex());
            if (offset != null) {
                Vector3i secondPartPosition = AutoIgnitionMathUtil.getRelativePosition(position, offset);

                WorldChunk chunk = AutoIgnitionChunkUtil.getSafeChunkFromBlock(world, secondPartPosition);
                if (chunk != null && isSameBenchType(bench, chunk, secondPartPosition)) {
                    scanAt(world, secondPartPosition, cache);
                }
            }
        }
    }

    /**
     * Verifies if a block at a given position is part of the same bench instance.
     * @param bench The original bench state to compare with.
     * @param chunk The chunk containing the block to check.
     * @param position The world position of the block to check.
     * @return true if the block shares the same ID.
     */
    @SuppressWarnings("removal")
    private static boolean isSameBenchType(ProcessingBenchState bench, WorldChunk chunk, Vector3i position) {
        BlockType firstPartType = bench.getBlockType();
        BlockType secondPartType = chunk.getBlockType(position);
        if (secondPartType == null || firstPartType == null) return false;
        return secondPartType.getId().equals(firstPartType.getId());
    }

    /**
     * Scans all 4 cardinal directions around a specific position for valid containers.
     * @param world The world instance.
     * @param position The central position to scan around.
     * @param cache The cache to populate.
     */
    private static void scanAt(World world, Vector3i position, BenchCache cache) {
        for (Vector3i offset : HORIZONTAL_OFFSETS) {
            Vector3i neighborPosition = AutoIgnitionMathUtil.getRelativePosition(position, offset);

            WorldChunk chunk = AutoIgnitionChunkUtil.getSafeChunkFromBlock(world, neighborPosition);
            if (chunk == null) continue;

            BlockType neighborType = chunk.getBlockType(neighborPosition);
            if (neighborType != null && neighborType.getState() instanceof ItemContainerState.ItemContainerStateData) {
                if (!cache.getContainerPositions().contains(neighborPosition)) {
                    cache.getContainerPositions().add(neighborPosition);
                }

                if (neighborType.getId().contains("Large")) {
                    scanSecondChestPart(world, neighborPosition, cache, neighborType.getState());
                }
            }
        }
    }

    /**
     * Specifically scans for the second part of a multi-block container
     * based on its shared StateData.
     * @param world The world instance.
     * @param position The position of the first part of the container.
     * @param cache The cache to populate.
     * @param originalState The {@link StateData} of the first part to find its match.
     */
    private static void scanSecondChestPart(World world, Vector3i position, BenchCache cache, StateData originalState) {
        for (Vector3i offset : HORIZONTAL_OFFSETS) {
            Vector3i neighborPosition = AutoIgnitionMathUtil.getRelativePosition(position, offset);
            WorldChunk chunk = AutoIgnitionChunkUtil.getSafeChunkFromBlock(world, neighborPosition);

            if (chunk != null) {
                BlockType neighborType = chunk.getBlockType(neighborPosition);
                if (neighborType != null && originalState.equals(neighborType.getState())) {
                    if (!cache.getContainerPositions().contains(neighborPosition)) {
                        cache.getContainerPositions().add(neighborPosition);
                    }
                }
            }
        }
    }
}
