package gg.moonflower.locksmith.api.lock.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gg.moonflower.locksmith.api.lock.AbstractLock;
import gg.moonflower.locksmith.core.registry.LocksmithLocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SerializableUUID;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.UUID;

public class CodeLock extends AbstractLock {
    public static final Codec<CodeLock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SerializableUUID.CODEC.fieldOf("id").forGetter(CodeLock::getId),
            BlockPos.CODEC.fieldOf("pos").forGetter(CodeLock::getPos),
            ItemStack.CODEC.fieldOf("stack").forGetter(CodeLock::getStack),
            Codec.STRING.fieldOf("code").forGetter(CodeLock::getCode),
            Codec.list(SerializableUUID.CODEC).fieldOf("authorized").forGetter(CodeLock::getAuthorized)
    ).apply(instance, CodeLock::new));

    private final List<UUID> authorized;
    private String code;

    public CodeLock(UUID id, BlockPos pos, ItemStack stack, String code, List<UUID> authorized) {
        super(LocksmithLocks.CODE.get(), id, pos, stack);
        this.code = code;
        this.authorized = authorized;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UUID> getAuthorized() {
        return this.authorized;
    }

    public boolean isAuthorized(UUID uuid) {
        return this.authorized.contains(uuid);
    }

    public void addAuthorized(UUID uuid) {
        this.authorized.add(uuid);
    }

    public void removeAuthorized(UUID uuid) {
        this.authorized.remove(uuid);
    }

    @Override
    public boolean canRemove(Player player, Level level, ItemStack stack) {
        return player.isSecondaryUseActive() && this.isAuthorized(player.getUUID());
    }

    @Override
    public boolean onRightClick(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (this.canRemove(player, level, player.getItemInHand(hand))) {
            this.onRemove(level);
            return true;
        }
        return false;
    }

    @Override
    public boolean onLeftClick(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction) {
        return false;
    }

    @Override
    public void onLockpick(UseOnContext ctx) {
    }
}
