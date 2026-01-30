package net.autoignition.inventory;

import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import net.autoignition.cache.BenchCache;

/**
 * Provides spatial scanning logic to identify neighboring storage containers.
 */
public class NeighborScanner {
    // TODO: Fix the positions for detection
    private static final Vector3i[] OFFSETS = {
            new Vector3i(1, 0, 0),
            new Vector3i(-1, 0, 0),
            new Vector3i(0, 1, 0),
            new Vector3i(0, -1, 0),
            new Vector3i(0, 0, 1),
            new Vector3i(0, 0, -1)
    };

    /**
     * Performs a neighborhood scan to locate valid item containers.
     * Discovered positions are stored in the provided {@link BenchCache}.
     * @param world The world instance.
     * @param position The bench's position.
     * @param cache The cache to be updated with new container positions.
     */
    public static void scan(World world, Vector3i position, BenchCache cache) {
        cache.getContainerPositions().clear();
        ChunkStore store = world.getChunkStore();

        for (Vector3i offset : OFFSETS) {
            int x = position.x + offset.x;
            int y = position.y + offset.y;
            int z = position.z + offset.z;

            long chunkIndex = ChunkUtil.indexChunk(x >> 4, z >> 4);
            if (store.getChunkReference(chunkIndex) == null) continue;

            if (isValidContainer(world, x, y, z)) {
                cache.getContainerPositions().add(new Vector3i(x, y, z));
            }
        }

        cache.updateScanTime();
    }

    /**
     * Determines if a block at the specified coordinates is a container.
     * @param world The world instance.
     * @param x X-coordinate.
     * @param y Y-coordinate.
     * @param z Z-coordinate.
     * @return true if the block type is associated with an {@link ItemContainerState}.
     */
    private static boolean isValidContainer(World world, int x, int y, int z) {
        BlockType type = world.getBlockType(x, y, z);
        return type != null && type.getState() instanceof ItemContainerState.ItemContainerStateData;
    }
}
