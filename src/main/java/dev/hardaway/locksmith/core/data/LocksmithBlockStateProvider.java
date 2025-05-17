package dev.hardaway.locksmith.core.data;

import dev.hardaway.locksmith.core.Locksmith;
import dev.hardaway.locksmith.core.registry.LocksmithBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class LocksmithBlockStateProvider extends BlockStateProvider {
    public LocksmithBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Locksmith.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        TextureMapping texture = this.createTableBlock(LocksmithBlocks.LOCKSMITHING_TABLE);
        this.simpleBlock(LocksmithBlocks.LOCKSMITHING_TABLE.get(),  this.models().withExistingParent("block/locksmithing_table", "cube")
                .texture("particle", texture.get(TextureSlot.PARTICLE))
                .texture("down", texture.get(TextureSlot.DOWN))
                .texture("up", texture.get(TextureSlot.UP))
                .texture("north", texture.get(TextureSlot.NORTH))
                .texture("south", texture.get(TextureSlot.SOUTH))
                .texture("east", texture.get(TextureSlot.EAST))
                .texture("west", texture.get(TextureSlot.WEST))
        );
    }

    private TextureMapping createTableBlock(Supplier<Block> block) {
        return new TextureMapping()
                .put(TextureSlot.PARTICLE, this.blockTextureWithSuffix(block, "_front"))
                .put(TextureSlot.DOWN, this.blockTextureWithSuffix(block, "_bottom"))
                .put(TextureSlot.UP, this.blockTextureWithSuffix(block, "_top"))
                .put(TextureSlot.NORTH, this.blockTextureWithSuffix(block, "_front"))
                .put(TextureSlot.EAST, this.blockTextureWithSuffix(block, "_side"))
                .put(TextureSlot.SOUTH, this.blockTextureWithSuffix(block, "_front"))
                .put(TextureSlot.WEST, this.blockTextureWithSuffix(block, "_side"));
    }

    private ResourceLocation blockTextureWithSuffix(Supplier<Block> block, String suffix) {
        ResourceLocation blockTexture = this.blockTexture(block.get());
        return ResourceLocation.fromNamespaceAndPath(blockTexture.getNamespace(), blockTexture.getPath() + suffix);
    }
}
