package gg.moonflower.locksmith.core.registry;

import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class LocksmithBlocks {
    public static final PollinatedRegistry<Block> BLOCKS = PollinatedRegistry.create(Registry.BLOCK, Locksmith.MOD_ID);

    public static final Supplier<Block> LOCKSMITHING_TABLE = registerBlock("locksmithing_table", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));

    private static Supplier<Block> registerBlock(String id, Supplier<Block> block, Item.Properties properties) {
        Supplier<Block> register = BLOCKS.register(id, block);
        LocksmithItems.ITEMS.register(id, () -> new BlockItem(register.get(), properties));
        return register;
    }
}
