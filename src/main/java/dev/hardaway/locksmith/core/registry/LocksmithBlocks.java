package dev.hardaway.locksmith.core.registry;

import dev.hardaway.locksmith.core.Locksmith;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class  LocksmithBlocks {

    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(Locksmith.MOD_ID);

    public static final DeferredBlock<Block> LOCKSMITHING_TABLE = LocksmithBlocks.registerWithItem("locksmithing_table", () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.5F)
            .sound(SoundType.WOOD)
            .ignitedByLava()
    ), new Item.Properties());


    /**
     * Registers a block with a simple item.
     *
     * @param id         The id of the block
     * @param block      The block to register
     * @param properties The properties of the item to register
     * @param <R>        The type of block being registered
     * @return The registered block
     */
    private static <R extends Block> DeferredBlock<R> registerWithItem(String id, Supplier<R> block, Item.Properties properties) {
        return registerWithItem(id, block, object -> new BlockItem(object, properties));
    }

    /**
     * Registers a block with an item.
     *
     * @param id          The id of the block
     * @param block       The block to register
     * @param itemFactory The factory to create a new item from the registered block
     * @param <R>         The type of block being registered
     * @return The registered block
     */
    private static <R extends Block> DeferredBlock<R> registerWithItem(String id, Supplier<R> block, Function<R, Item> itemFactory) {
        DeferredBlock<R> register = REGISTRY.register(id, block);
        LocksmithItems.REGISTRY.register(id, () -> itemFactory.apply(register.get()));
        return register;
    }

}
