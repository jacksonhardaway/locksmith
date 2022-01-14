package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.common.block.LockButtonBlock;
import gg.moonflower.locksmith.common.block.LocksmithingTableBlock;
import gg.moonflower.locksmith.common.item.LockButtonItem;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Function;
import java.util.function.Supplier;

public class LocksmithBlocks {
    public static final PollinatedRegistry<Block> BLOCKS = PollinatedRegistry.create(Registry.BLOCK, Locksmith.MOD_ID);

    public static final Supplier<Block> LOCKSMITHING_TABLE = register("locksmithing_table", () -> new LocksmithingTableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    public static final Supplier<Block> LOCK_BUTTON = register("lock_button", () -> new LockButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F)), (block) -> new LockButtonItem(block.get(), new Item.Properties()));

    /**
     * Registers the specified block with a bound {@link BlockItem} under the specified id.
     *
     * @param name           The id of the block
     * @param block          The block to register
     * @param itemProperties The properties of the block item to register
     * @return The registry reference
     */
    private static <T extends Block> Supplier<T> register(String name, Supplier<T> block, Item.Properties itemProperties)
    {
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
    private static <T extends Block> Supplier<T> register(String name, Supplier<T> block, Function<Supplier<T>, Item> item)
    {
        Supplier<T> object = BLOCKS.register(name, block);
        LocksmithItems.ITEMS.register(name, () -> item.apply(object));
        return object;
    }
}
