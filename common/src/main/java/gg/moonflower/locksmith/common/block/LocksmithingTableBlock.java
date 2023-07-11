package gg.moonflower.locksmith.common.block;

import gg.moonflower.locksmith.common.menu.LocksmithingTableMenu;
import gg.moonflower.locksmith.core.Locksmith;
import gg.moonflower.locksmith.core.registry.LocksmithStats;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class LocksmithingTableBlock extends Block {

    private static final Component CONTAINER_TITLE = Component.translatable("container." + Locksmith.MOD_ID + ".locksmithing_table");

    public LocksmithingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide())
            return InteractionResult.SUCCESS;
        player.openMenu(state.getMenuProvider(level, pos));
//        player.awardStat(LocksmithStats.INTERACT_WITH_LOCKSMITHING_TABLE);
        return InteractionResult.CONSUME;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((containerId, inventory, player) -> new LocksmithingTableMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }
}