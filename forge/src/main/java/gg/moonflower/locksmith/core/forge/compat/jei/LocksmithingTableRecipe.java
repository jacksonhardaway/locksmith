package gg.moonflower.locksmith.core.forge.compat.jei;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class LocksmithingTableRecipe {

    private final List<List<ItemStack>> inputs;
    private final List<List<ItemStack>> outputs;

    public LocksmithingTableRecipe(List<ItemStack> topInputs, List<ItemStack> bottomInputs, List<ItemStack> leftOutputs, List<ItemStack> rightOutputs) {
        this.inputs = ImmutableList.of(topInputs, bottomInputs);
        this.outputs = ImmutableList.of(leftOutputs, rightOutputs);
    }

    public List<List<ItemStack>> getInputs() {
        return inputs;
    }

    public List<List<ItemStack>> getOutputs() {
        return outputs;
    }
}
