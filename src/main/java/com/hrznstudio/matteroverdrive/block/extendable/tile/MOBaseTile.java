package com.hrznstudio.matteroverdrive.block.extendable.tile;

import com.hrznstudio.matteroverdrive.block.extendable.block.MOBaseTileBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public abstract class MOBaseTile<T extends MOBaseTile<T>> extends BlockEntity implements ITickableBlockEntity<T>, MenuProvider {

  private final MOBaseTileBlock<T> base;
  private final Component title;

  public MOBaseTile(MOBaseTileBlock<T> base, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, Component title) {
    super(blockEntityType, pos, state);
    this.base = base;
    this.title = title;
  }

  public void onNeighborChanged(Block originBlock, BlockPos originPos) {}

  public InteractionResult onActivated(Player player, InteractionHand hand, Direction hitDirection, double hitX, double hitY, double hitZ) {
    return InteractionResult.PASS;
  }

  @Override
  public void setLevel(Level level) {
    super.setLevel(level);
    if (isClient()) {
      initClient();
    }
  }

  public void initClient() {}

  public void markForUpdate() {
    this.level.sendBlockUpdated(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()), 3);
    setChanged();
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag compoundTag = new CompoundTag();
    saveAdditional(compoundTag);
    return compoundTag;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    if (pkt.getTag() != null) {
      load(pkt.getTag());
    }
  }

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    CompoundTag tag = new CompoundTag();
    saveAdditional(tag);
    return ClientboundBlockEntityDataPacket.create(this, blockEntity -> tag);
  }

  public void updateNeigh() {
    this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
    this.level.sendBlockUpdated(this.worldPosition, this.level.getBlockState(worldPosition), this.level.getBlockState(worldPosition), 3);
  }

  public boolean isClient() {
    return this.level.isClientSide;
  }

  public boolean isServer() {
    return !isClient();
  }

  public MOBaseTileBlock<T> getBase() {
    return base;
  }

  @Override
  public Component getDisplayName() {
    return this.title;
  }

  public Component getTitle() {
    return this.title;
  }

  public void openGui(Player player) {
    if (player instanceof ServerPlayer serverPlayer) {
      if (this.createMenu(0, serverPlayer.getInventory(), serverPlayer) == null) return;
      NetworkHooks.openScreen(serverPlayer, this, byteBuf -> {});
    }
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int menuId, Inventory inventory, Player player) {
    return null;
  }

  @Nonnull
  public abstract T getSelf();

  public Level getLevel() {
    return getSelf().level;
  }

  public ContainerLevelAccess getWorldPosCallable() {
    return this.getLevel() != null  ? ContainerLevelAccess.create(this.getLevel(), this.getBlockPos()) : ContainerLevelAccess.NULL;
  }

  public boolean canInteract() {
    return this.getLevel().getBlockEntity(this.getBlockPos()) == this;
  }

}
