package com.teamacronymcoders.matteroverdrive.block.extendable.block;

import com.teamacronymcoders.matteroverdrive.api.raytrace.DistanceRayTraceResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public abstract class MOBaseBlock extends Block {

  private final CreativeModeTab tab;

  public MOBaseBlock(Properties properties) {
    super(properties);
    this.tab = CreativeModeTab.TAB_SEARCH;
  }

  public MOBaseBlock(Properties properties, CreativeModeTab tab) {
    super(properties);
    this.tab = tab;
  }

  @Nullable
  protected static DistanceRayTraceResult rayTraceBox(BlockPos pos, Vec3 start, Vec3 end, VoxelShape shape) {
    BlockHitResult bbResult = shape.clip(start, end, pos);
    if (bbResult != null) {
      Vec3 hitVec = bbResult.getLocation();
      Direction sideHit = bbResult.getDirection();
      double dist = start.distanceTo(hitVec);
      return new DistanceRayTraceResult(hitVec, sideHit, pos, shape, dist);
    }
    return null;
  }

  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    if (hasCustomBoxes(state, level, pos)) {
      VoxelShape shape = Shapes.empty();
      for (VoxelShape shape1 : getBoundingBoxes(state, level, pos)) {
        shape = Shapes.join(shape, shape1, BooleanOp.OR);
      }
      return shape;
    }
    return super.getCollisionShape(state, level, pos, context);
  }

  public Supplier<Item> getItemBlockFactory() {
    return () -> (Item) new BlockItem(this, new Item.Properties().tab(this.tab));
  }

  public List<VoxelShape> getBoundingBoxes(BlockState state, BlockGetter source, BlockPos pos) {
    return Collections.emptyList();
  }

  public boolean hasCustomBoxes(BlockState state, BlockGetter source, BlockPos pos) {
    return false;
  }

  @Nullable
  protected HitResult rayTraceBoxesClosest(Vec3 start, Vec3 end, BlockPos pos, List<VoxelShape> boxes) {
    List<DistanceRayTraceResult> results = new ArrayList<>();
    for (VoxelShape box : boxes) {
      DistanceRayTraceResult hit = rayTraceBox(pos, start, end, box);
      if (hit != null)
        results.add(hit);
    }
    HitResult closestHit = null;
    double curClosest = Double.MAX_VALUE;
    for (DistanceRayTraceResult hit : results) {
      if (curClosest > hit.getDistance()) {
        closestHit = hit;
        curClosest = hit.getDistance();
      }
    }
    return closestHit;
  }

  @Override
  @SuppressWarnings("deprecation")
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (!state.is(newState.getBlock())) {
      Containers.dropContents(worldIn, pos, getDynamicDrops(state, worldIn, pos, newState, isMoving));
      worldIn.updateNeighbourForOutputSignal(pos, this);
    }
    super.onRemove(state, worldIn, pos, newState, isMoving);
  }

  public NonNullList<ItemStack> getDynamicDrops(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    NonNullList<ItemStack> stacks = NonNullList.create();
    BlockEntity blockEntity = worldIn.getBlockEntity(pos);
    // TODO: Fix the below code
//    if (blockEntity instanceof ActiveTile && ((ActiveTile<?>) blockEntity).getMultiInventoryComponent() != null) {
//      for (InventoryComponent<?> inventoryHandler : ((ActiveTile<?>) blockEntity).getMultiInventoryComponent().getInventoryHandlers()) {
//        for (int i = 0; i < inventoryHandler.getSlots(); i++) {
//          stacks.add(inventoryHandler.getStackInSlot(i));
//        }
//      }
//    }
    return stacks;
  }

  public boolean hasIndividualRenderVoxelShape() {
    return false;
  }

}
