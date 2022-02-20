package gg.moonflower.locksmith.common.item;

import com.google.gson.JsonParseException;
import gg.moonflower.locksmith.api.lock.LockManager;
import gg.moonflower.locksmith.common.lock.types.KeyLock;
import gg.moonflower.locksmith.core.registry.LocksmithParticles;
import gg.moonflower.locksmith.core.registry.LocksmithSounds;
import gg.moonflower.locksmith.core.registry.LocksmithTags;
import gg.moonflower.pollen.api.util.NbtConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LockItem extends Item {

    public LockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        UUID lockId = KeyItem.getLockId(stack);
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (LockManager.getLock(level, pos) != null || lockId == null || !level.getBlockState(pos).is(LocksmithTags.LOCKABLE))
            return InteractionResult.PASS;

        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        ItemStack lockStack = stack.copy();
        lockStack.setCount(1);
        LockManager.get(level).addLock(new KeyLock(lockId, pos, lockStack));
        level.playSound(null, pos, LocksmithSounds.ITEM_LOCK_PLACE.get(), SoundSource.BLOCKS, 0.75F, 1.0F);

        level.getBlockState(pos).getVisualShape(level, pos, CollisionContext.empty()).forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            double d1 = Math.min(1.0, maxX - minX);
            double d2 = Math.min(1.0, maxY - minY);
            double d3 = Math.min(1.0, maxZ - minZ);
            int i = Math.max(2, Mth.ceil(d1 / 0.4));
            int j = Math.max(2, Mth.ceil(d2 / 0.4));
            int k = Math.max(2, Mth.ceil(d3 / 0.4));

            ((ServerLevel) level).sendParticles(LocksmithParticles.LOCK_SPARK.get(), pos.getX() + minX + maxX / 2.0, pos.getY() + minY + maxY / 2.0, pos.getZ() + minZ + maxZ / 2.0, i * j * k, (maxX - minX) / 4.0 + 0.0625, (maxY - minY) / 4.0 + 0.0625, (maxZ - minZ) / 4.0 + 0.0625, 0.0);
        });

        Player player = context.getPlayer();
        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.isCreative())
                stack.shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        getKeyName(stack).ifPresent(name -> tooltipComponents.add(new TranslatableComponent(this.getDescriptionId(stack) + ".key", name.copy().withStyle(ChatFormatting.GRAY))));
        if (isAdvanced.isAdvanced()) {
            UUID id = KeyItem.getLockId(stack);
            if (id == null)
                return;

            tooltipComponents.add(new TextComponent("Lock Id: " + id).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public static Optional<Component> getKeyName(ItemStack stack) {
        if (!(stack.getItem() instanceof LockItem))
            return Optional.empty();
        CompoundTag nbt = stack.getTag();
        if (nbt == null || !nbt.contains("KeyName", NbtConstants.STRING))
            return Optional.empty();

        try {
            Component component = Component.Serializer.fromJson(nbt.getString("KeyName"));
            if (component != null)
                return Optional.of(component);

            nbt.remove("KeyName");
        } catch (JsonParseException e) {
            nbt.remove("KeyName");
        }

        return Optional.empty();
    }

    public static void setKeyName(ItemStack stack, @Nullable Component name) {
        if (!(stack.getItem() instanceof LockItem))
            return;

        if (name == null) {
            stack.removeTagKey("KeyName");
            return;
        }

        stack.getOrCreateTag().putString("KeyName", Component.Serializer.toJson(name));
    }
}
