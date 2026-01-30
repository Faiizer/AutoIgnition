package net.autoignition.inventory;

import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
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
     * Scans the surrounding area of a processing bench to identify and cache nearby item containers.
     * <p>This method performs a primary scan around the initial block and then checks
     * immediate neighbors for blocks sharing the same {@link ProcessingBenchState}.
     * This ensures that multi-block structures (like 2-block long furnaces) have their
     * entire footprint scanned for adjacent storage.</p>
     *
     * @param world The world instance where the scan is performed.
     * @param position The bench's position.
     * @param cache The {@link BenchCache} where discovered container positions are stored.
     * @param bench The state instance used to identify connected blocks of the same bench.
     */
    @SuppressWarnings("deprecation")
    public static void scan(World world, Vector3i position, BenchCache cache, ProcessingBenchState bench) {
        cache.getContainerPositions().clear();
        ChunkStore store = world.getChunkStore();

        scanAt(world, position, store, cache);

        for (Vector3i offset : OFFSETS) {
            Vector3i neighborPos = new Vector3i(position.x + offset.x, position.y + offset.y, position.z + offset.z);

            if (world.getState(neighborPos.x, neighborPos.y, neighborPos.z, true) == bench) {
                scanAt(world, neighborPos, store, cache);
            }
        }

        cache.updateScanTime();
    }

    /**
     * Internal helper to check the 4 cardinal directions around a specific coordinate for containers.
     * @param world The world instance.
     * @param pos The coordinate to scan around.
     * @param store The ChunkStore to verify chunk loading states.
     * @param cache The cache to populate with found containers.
     */
    private static void scanAt(World world, Vector3i pos, ChunkStore store, BenchCache cache) {
        for (Vector3i offset : OFFSETS) {
            int x = pos.x + offset.x;
            int y = pos.y + offset.y;
            int z = pos.z + offset.z;

            long chunkIndex = ChunkUtil.indexChunk(x >> 4, z >> 4);
            if (store.getChunkReference(chunkIndex) == null) continue;

            if (isValidContainer(world, x, y, z)) {
                cache.getContainerPositions().add(new Vector3i(x, y, z));
            }
        }
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
