package gg.moonflower.locksmith.core.mixin;

import gg.moonflower.locksmith.core.extension.ChestBlockExtension;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

public class ChestBlockMixin {
    @Mixin(targets = {"net/minecraft/world/level/block/ChestBlock$2"})
    static class DoubleBlockCombinerMixin {
        @Inject(method = "acceptDouble(Lnet/minecraft/world/level/block/entity/ChestBlockEntity;Lnet/minecraft/world/level/block/entity/ChestBlockEntity;)Ljava/util/Optional;", at = @At("HEAD"))
        public void acceptDouble(ChestBlockEntity chestBlockEntity, ChestBlockEntity chestBlockEntity2, CallbackInfoReturnable<Optional<MenuProvider>> cir) {
            ChestBlockExtension.chestBlockEntity1.set(chestBlockEntity);
            ChestBlockExtension.chestBlockEntity2.set(chestBlockEntity2);
        }
    }

    @Mixin(targets = {"net/minecraft/world/level/block/ChestBlock$2$1"})
    static class MenuProviderMixin {
        @Inject(method = "getDisplayName()Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
        public void getDisplayName(CallbackInfoReturnable<Component> cir) {
            if (ChestBlockExtension.chestBlockEntity1.get().hasCustomName() || ChestBlockExtension.chestBlockEntity2.get().hasCustomName())
                return;

            cir.setReturnValue(new TranslatableComponent("container.locksmith.locked", cir.getReturnValue()));
        }
    }
}
