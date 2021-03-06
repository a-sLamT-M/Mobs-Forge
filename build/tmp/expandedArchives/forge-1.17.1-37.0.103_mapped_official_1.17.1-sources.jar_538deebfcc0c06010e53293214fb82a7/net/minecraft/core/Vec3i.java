package net.minecraft.core;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import java.util.stream.IntStream;
import javax.annotation.concurrent.Immutable;
import net.minecraft.Util;
import net.minecraft.util.Mth;

@Immutable
public class Vec3i implements Comparable<Vec3i> {
   public static final Codec<Vec3i> CODEC = Codec.INT_STREAM.comapFlatMap((p_123318_) -> {
      return Util.fixedSize(p_123318_, 3).map((p_175586_) -> {
         return new Vec3i(p_175586_[0], p_175586_[1], p_175586_[2]);
      });
   }, (p_123313_) -> {
      return IntStream.of(p_123313_.getX(), p_123313_.getY(), p_123313_.getZ());
   });
   public static final Vec3i ZERO = new Vec3i(0, 0, 0);
   private int x;
   private int y;
   private int z;

   public Vec3i(int p_123296_, int p_123297_, int p_123298_) {
      this.x = p_123296_;
      this.y = p_123297_;
      this.z = p_123298_;
   }

   public Vec3i(double p_123292_, double p_123293_, double p_123294_) {
      this(Mth.floor(p_123292_), Mth.floor(p_123293_), Mth.floor(p_123294_));
   }

   public boolean equals(Object p_123327_) {
      if (this == p_123327_) {
         return true;
      } else if (!(p_123327_ instanceof Vec3i)) {
         return false;
      } else {
         Vec3i vec3i = (Vec3i)p_123327_;
         if (this.getX() != vec3i.getX()) {
            return false;
         } else if (this.getY() != vec3i.getY()) {
            return false;
         } else {
            return this.getZ() == vec3i.getZ();
         }
      }
   }

   public int hashCode() {
      return (this.getY() + this.getZ() * 31) * 31 + this.getX();
   }

   public int compareTo(Vec3i p_123330_) {
      if (this.getY() == p_123330_.getY()) {
         return this.getZ() == p_123330_.getZ() ? this.getX() - p_123330_.getX() : this.getZ() - p_123330_.getZ();
      } else {
         return this.getY() - p_123330_.getY();
      }
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   protected Vec3i setX(int p_175605_) {
      this.x = p_175605_;
      return this;
   }

   protected Vec3i setY(int p_175604_) {
      this.y = p_175604_;
      return this;
   }

   protected Vec3i setZ(int p_175603_) {
      this.z = p_175603_;
      return this;
   }

   public Vec3i offset(double p_175587_, double p_175588_, double p_175589_) {
      return p_175587_ == 0.0D && p_175588_ == 0.0D && p_175589_ == 0.0D ? this : new Vec3i((double)this.getX() + p_175587_, (double)this.getY() + p_175588_, (double)this.getZ() + p_175589_);
   }

   public Vec3i offset(int p_175593_, int p_175594_, int p_175595_) {
      return p_175593_ == 0 && p_175594_ == 0 && p_175595_ == 0 ? this : new Vec3i(this.getX() + p_175593_, this.getY() + p_175594_, this.getZ() + p_175595_);
   }

   public Vec3i offset(Vec3i p_175597_) {
      return this.offset(p_175597_.getX(), p_175597_.getY(), p_175597_.getZ());
   }

   public Vec3i subtract(Vec3i p_175596_) {
      return this.offset(-p_175596_.getX(), -p_175596_.getY(), -p_175596_.getZ());
   }

   public Vec3i multiply(int p_175602_) {
      if (p_175602_ == 1) {
         return this;
      } else {
         return p_175602_ == 0 ? ZERO : new Vec3i(this.getX() * p_175602_, this.getY() * p_175602_, this.getZ() * p_175602_);
      }
   }

   public Vec3i above() {
      return this.above(1);
   }

   public Vec3i above(int p_123336_) {
      return this.relative(Direction.UP, p_123336_);
   }

   public Vec3i below() {
      return this.below(1);
   }

   public Vec3i below(int p_123335_) {
      return this.relative(Direction.DOWN, p_123335_);
   }

   public Vec3i north() {
      return this.north(1);
   }

   public Vec3i north(int p_175601_) {
      return this.relative(Direction.NORTH, p_175601_);
   }

   public Vec3i south() {
      return this.south(1);
   }

   public Vec3i south(int p_175600_) {
      return this.relative(Direction.SOUTH, p_175600_);
   }

   public Vec3i west() {
      return this.west(1);
   }

   public Vec3i west(int p_175599_) {
      return this.relative(Direction.WEST, p_175599_);
   }

   public Vec3i east() {
      return this.east(1);
   }

   public Vec3i east(int p_175598_) {
      return this.relative(Direction.EAST, p_175598_);
   }

   public Vec3i relative(Direction p_175592_) {
      return this.relative(p_175592_, 1);
   }

   public Vec3i relative(Direction p_123321_, int p_123322_) {
      return p_123322_ == 0 ? this : new Vec3i(this.getX() + p_123321_.getStepX() * p_123322_, this.getY() + p_123321_.getStepY() * p_123322_, this.getZ() + p_123321_.getStepZ() * p_123322_);
   }

   public Vec3i relative(Direction.Axis p_175590_, int p_175591_) {
      if (p_175591_ == 0) {
         return this;
      } else {
         int i = p_175590_ == Direction.Axis.X ? p_175591_ : 0;
         int j = p_175590_ == Direction.Axis.Y ? p_175591_ : 0;
         int k = p_175590_ == Direction.Axis.Z ? p_175591_ : 0;
         return new Vec3i(this.getX() + i, this.getY() + j, this.getZ() + k);
      }
   }

   public Vec3i cross(Vec3i p_123325_) {
      return new Vec3i(this.getY() * p_123325_.getZ() - this.getZ() * p_123325_.getY(), this.getZ() * p_123325_.getX() - this.getX() * p_123325_.getZ(), this.getX() * p_123325_.getY() - this.getY() * p_123325_.getX());
   }

   public boolean closerThan(Vec3i p_123315_, double p_123316_) {
      return this.distSqr((double)p_123315_.getX(), (double)p_123315_.getY(), (double)p_123315_.getZ(), false) < p_123316_ * p_123316_;
   }

   public boolean closerThan(Position p_123307_, double p_123308_) {
      return this.distSqr(p_123307_.x(), p_123307_.y(), p_123307_.z(), true) < p_123308_ * p_123308_;
   }

   public double distSqr(Vec3i p_123332_) {
      return this.distSqr((double)p_123332_.getX(), (double)p_123332_.getY(), (double)p_123332_.getZ(), true);
   }

   public double distSqr(Position p_123310_, boolean p_123311_) {
      return this.distSqr(p_123310_.x(), p_123310_.y(), p_123310_.z(), p_123311_);
   }

   public double distSqr(Vec3i p_175583_, boolean p_175584_) {
      return this.distSqr((double)p_175583_.x, (double)p_175583_.y, (double)p_175583_.z, p_175584_);
   }

   public double distSqr(double p_123300_, double p_123301_, double p_123302_, boolean p_123303_) {
      double d0 = p_123303_ ? 0.5D : 0.0D;
      double d1 = (double)this.getX() + d0 - p_123300_;
      double d2 = (double)this.getY() + d0 - p_123301_;
      double d3 = (double)this.getZ() + d0 - p_123302_;
      return d1 * d1 + d2 * d2 + d3 * d3;
   }

   public int distManhattan(Vec3i p_123334_) {
      float f = (float)Math.abs(p_123334_.getX() - this.getX());
      float f1 = (float)Math.abs(p_123334_.getY() - this.getY());
      float f2 = (float)Math.abs(p_123334_.getZ() - this.getZ());
      return (int)(f + f1 + f2);
   }

   public int get(Direction.Axis p_123305_) {
      return p_123305_.choose(this.x, this.y, this.z);
   }

   public String toString() {
      return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
   }

   public String toShortString() {
      return this.getX() + ", " + this.getY() + ", " + this.getZ();
   }
}