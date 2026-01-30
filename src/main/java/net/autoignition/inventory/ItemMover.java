package net.autoignition.inventory;

import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.CombinedItemContainer;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState;
import net.autoignition.AutoIgnitionMod;
import net.autoignition.cache.BenchCache;

import java.util.List;
import java.util.logging.Level;

/**
 * Handles the physical movement of items between benches and external storage.
 * Manages both refueling logic and output extraction by iterating through
 * discovered nearby containers.
 */
public class ItemMover {

    // TODO: Wait for new methods
    /**
     * Attempts to refill the bench's fuel slot from cached nearby containers.
     * @param bench The bench requiring fuel.
     * @param world The world instance.
     * @param cache The bench's cache.
     */
    @SuppressWarnings("deprecation")
    public static void refillFuel(ProcessingBenchState bench, World world, BenchCache cache) {
        CombinedItemContainer combinedItemContainer = bench.getItemContainer();
        ItemContainer fuelContainer = combinedItemContainer.getContainer(0);

        if (!fuelContainer.isEmpty()) return;

        for (Vector3i position : cache.getContainerPositions()) {
            if (world.getState(position.x, position.y, position.z, true) instanceof ItemContainerState itemContainerState) {
                ItemContainer chestContainer = itemContainerState.getItemContainer();

                transferFuel(chestContainer, fuelContainer);

                if (!fuelContainer.isEmpty()) break;
            }
        }
    }

    /**
     * Internal logic for moving fuel items from a source to a destination.
     * Validates item quality and checks against the configuration blacklist.
     * @param source The external storage container.
     * @param destination The bench's internal fuel container.
     */
    private static void transferFuel(ItemContainer source, ItemContainer destination) {
        if (destination == null || destination.getCapacity() == 0) return;

        for (short i = 0; i < source.getCapacity(); i++) {
            ItemStack itemStack = source.getItemStack(i);

            if (itemStack == null || itemStack.getItem().getFuelQuality() <= 0) continue;
            if (AutoIgnitionMod.getConfig().getBlacklistedFuelItems().contains(itemStack.getItemId())) continue;

            if (destination.canAddItemStack(itemStack)) {
                source.moveItemStackFromSlot(i, destination);
            }

            if (!destination.isEmpty()) return;
        }
    }

    // TODO: Wait for new methods
    /**
     * Automatically clears the bench's output slots and transfers contents to nearby storage.
     * @param bench The bench to empty.
     * @param world The world instance.
     * @param cache The bench's cache.
     */
    @SuppressWarnings("deprecation")
    public static void emptyOutput(ProcessingBenchState bench, World world, BenchCache cache) {
        CombinedItemContainer combinedItemContainer = bench.getItemContainer();
        ItemContainer outputContainer = combinedItemContainer.getContainer(2);

        if (outputContainer.isEmpty()) return;

        for (Vector3i position : cache.getContainerPositions()) {
            if (world.getState(position.x, position.y, position.z, true) instanceof ItemContainerState itemContainerState) {
                ItemContainer chestContainer = itemContainerState.getItemContainer();

                transferOutput(outputContainer, chestContainer);

                if (outputContainer.isEmpty()) break;
            }
        }
    }

    /**
     * Internal logic for moving output items to external storage.
     * @param source The bench's internal output container.
     * @param destination The external storage container.
     */
    private static void transferOutput(ItemContainer source, ItemContainer destination) {
        for (short i = 0; i < source.getCapacity(); i++) {
            ItemStack itemStack = source.getItemStack(i);

            if (itemStack != null) {
                if (destination.canAddItemStack(itemStack)) {
                    source.moveItemStackFromSlot(i, destination);
                }
            }
        }
    }

    // TODO: Wait for new methods
    /**
     * Automatically fills the bench's input slots from nearby storage.
     * @param bench The bench to fill the input.
     * @param world The world instance.
     * @param cache The bench's cache.
     */
    @SuppressWarnings("deprecation")
    public static void refillInput(ProcessingBenchState bench, World world, BenchCache cache) {
        CombinedItemContainer combinedItemContainer = bench.getItemContainer();
        ItemContainer inputContainer = combinedItemContainer.getContainer(1);

        for (Vector3i position : cache.getContainerPositions()) {
            if (world.getState(position.x, position.y, position.z, true) instanceof ItemContainerState itemContainerState) {
                ItemContainer chestContainer = itemContainerState.getItemContainer();

                transferInput(chestContainer, inputContainer);
            }
        }
    }

    /**
     * Internal logic for moving input items from external storage.
     * @param source The external storage container.
     * @param destination The bench's internal input container.
     */
    private static void transferInput(ItemContainer source, ItemContainer destination) {
        for (short i = 0; i < source.getCapacity(); i++) {
            ItemStack itemStack = source.getItemStack(i);
            if (itemStack != null) {
                if (AutoIgnitionMod.getConfig().getBlacklistedInputItems().contains(itemStack.getItemId())) continue;

                if (destination.canAddItemStack(itemStack)) {
                    source.moveItemStackFromSlot(i, destination);
                }
            }
        }
    }
}