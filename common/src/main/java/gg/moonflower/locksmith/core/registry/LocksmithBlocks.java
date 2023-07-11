package gg.moonflower.locksmith.core.registry;

import dev.architectury.registry.registries.RegistrySupplier;
import gg.moonflower.locksmith.common.block.LockButtonBlock;
import gg.moonflower.locksmith.common.block.LocksmithingTableBlock;
import gg.moonflower.locksmith.common.item.LockButtonItem;
import gg.moonflower.pollen.api.registry.wrapper.v1.PollinatedBlockRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Function;
import java.util.function.Supplier;

public class LocksmithBlocks {
    public static final PollinatedBlockRegistry REGISTRY = PollinatedBlockRegistry.create(LocksmithItems.REGISTRY);

    public static final RegistrySupplier<Block> LOCKSMITHING_TABLE = register("locksmithing_table", () -> new LocksmithingTableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    public static final RegistrySupplier<Block> LOCK_BUTTON = register("lock_button", () -> new LockButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F)), (block) -> new LockButtonItem(block.get(), new Item.Properties()));

    /**
     * Registers the specified block with a bound {@link BlockItem} under the specified id.
     *
     * @param name           The id of the block
     * @param block          The block to register
     * @param itemProperties The properties of the block item to register
     * @return The registry reference
     */
    private static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block, Item.Properties itemProperties) {
        return register(name, block, object -> new BlockItem(object.get(), itemProperties));
    }

    /**
     * Registers the specified block with a bound item under the specified id.
     *
     * @param name  The id of the block
     * @param block The block to register
     * @param item  The item to register or null for no item
     * @return The registry reference
     */
    private static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block, Function<Supplier<T>, Item> item) {
        RegistrySupplier<T> object = REGISTRY.register(name, block);
        LocksmithItems.REGISTRY.register(name, () -> item.apply(object));
        return object;
    }
}
