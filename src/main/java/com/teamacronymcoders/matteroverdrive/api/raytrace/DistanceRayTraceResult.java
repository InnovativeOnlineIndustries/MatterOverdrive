package com.teamacronymcoders.matteroverdrive.api.raytrace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DistanceRayTraceResult extends HitResult {

  private double distance;
  private VoxelShape hitBox;

  public DistanceRayTraceResult(Vec3 hitVector, Direction sideHit, BlockPos pos, VoxelShape shape, double distance) {
    super(hitVector);
    this.hitBox = shape;
    this.distance = distance;
  }

  public VoxelShape getHitBox() {
    return hitBox;
  }

  public double getDistance() {
    return distance;
  }

  @Override
  public Type getType() {
    return Type.BLOCK;
  }
}
