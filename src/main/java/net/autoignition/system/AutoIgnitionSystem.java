package net.autoignition.system;

import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.universe.world.chunk.ChunkFlag;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import net.autoignition.AutoIgnitionMod;
import net.autoignition.config.AutoIgnitionConfig;
import net.autoignition.inventory.BenchProcessor;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * Main ticking system for AutoIgnition.
 * Orchestrates the logic for every processing bench in loaded chunks.
 */
public class AutoIgnitionSystem extends EntityTickingSystem<ChunkStore> {

    private final ComponentType<ChunkStore, ProcessingBenchState> benchComponentType;

    public AutoIgnitionSystem(ComponentType<ChunkStore, ProcessingBenchState> benchComponentType) {
        this.benchComponentType = benchComponentType;
    }

    @SuppressWarnings("removal")
    @Override
    public void tick(
            float dt,
            int entityIndex,
            @Nonnull ArchetypeChunk<ChunkStore> archetypeChunk,
            @Nonnull Store<ChunkStore> store,
            @Nonnull CommandBuffer<ChunkStore> commandBuffer
    ) {
        ProcessingBenchState bench = archetypeChunk.getComponent(entityIndex, this.benchComponentType);
        if (bench == null) return;
        if (AutoIgnitionMod.getConfig().getBlacklistedProcessorBenches().contains(bench.getBaseBlockType().getId())) return;

        WorldChunk chunk = bench.getChunk();
        if (chunk == null) return;

        boolean isChunkTicking = chunk.is(ChunkFlag.TICKING);
        if (!isChunkTicking) return;

        BenchProcessor.handle(bench);
    }

    @Override
    public Query<ChunkStore> getQuery() {
        return this.benchComponentType;
    }
}