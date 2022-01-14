package gg.moonflower.locksmith.core.forge.compat.jei;

import com.google.common.collect.ImmutableList;
import gg.moonflower.locksmith.common.item.KeyItem;
import gg.moonflower.locksmith.common.recipe.LocksmithingRecipe;
import net.minecraft.Util;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ApiStatus.Internal
public class LocksmithingJeiRecipe {

    private final List<List<ItemStack>> inputs;
    private final List<List<ItemStack>> outputs;

    public LocksmithingJeiRecipe(LocksmithingRecipe recipe) {
        SimpleContainer container = new SimpleContainer(2);
        List<ItemStack> input1 = Arrays.stream(recipe.getTopInput().getItems()).peek(stack -> {
            KeyItem.setLockId(stack, Util.NIL_UUID);
            if (KeyItem.isKey(stack) && !KeyItem.isOriginal(stack))
                KeyItem.setOriginal(stack, true);
        }).collect(Collectors.toList());
        List<ItemStack> input2 = Arrays.asList(recipe.getBottomInput().getItems());

        List<ItemStack> output1 = new ArrayList<>();
        List<ItemStack> output2 = new ArrayList<>();
        for (ItemStack topInput : recipe.getTopInput().getItems()) {
            ItemStack topInputCopy = topInput.copy();
            if (KeyItem.isKey(topInputCopy) && !KeyItem.isOriginal(topInputCopy))
                KeyItem.setOriginal(topInputCopy, true);

            container.setItem(0, topInputCopy);
            for (ItemStack bottomInput : recipe.getTopInput().getItems()) {
                container.setItem(1, bottomInput);

                ItemStack stack1 = recipe.assemble(container);
                ItemStack stack2 = recipe.getSecondResultItem();

                KeyItem.setLockId(stack1, Util.NIL_UUID);
                if (output1.stream().noneMatch(s -> ItemStack.matches(s, stack1)))
                    output1.add(stack1);

                if (!stack2.isEmpty()) {
                    KeyItem.setLockId(stack2, Util.NIL_UUID);
                    if (output1.stream().noneMatch(s -> ItemStack.matches(s, stack2)))
                        output2.add(stack2);
                }
            }
        }

        this.inputs = ImmutableList.of(input1, input2);
        this.outputs = output2.isEmpty() ? ImmutableList.of(output1) : ImmutableList.of(output1, output2);
    }

    public List<List<ItemStack>> getInputs() {
        return inputs;
    }

    public List<List<ItemStack>> getOutputs() {
        return outputs;
    }
}
