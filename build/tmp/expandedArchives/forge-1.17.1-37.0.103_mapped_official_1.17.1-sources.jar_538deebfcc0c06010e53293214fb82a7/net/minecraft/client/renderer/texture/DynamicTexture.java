package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nullable;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class DynamicTexture extends AbstractTexture {
   private static final Logger LOGGER = LogManager.getLogger();
   @Nullable
   private NativeImage pixels;

   public DynamicTexture(NativeImage p_117984_) {
      this.pixels = p_117984_;
      if (!RenderSystem.isOnRenderThread()) {
         RenderSystem.recordRenderCall(() -> {
            TextureUtil.prepareImage(this.getId(), this.pixels.getWidth(), this.pixels.getHeight());
            this.upload();
         });
      } else {
         TextureUtil.prepareImage(this.getId(), this.pixels.getWidth(), this.pixels.getHeight());
         this.upload();
      }

   }

   public DynamicTexture(int p_117980_, int p_117981_, boolean p_117982_) {
      RenderSystem.assertThread(RenderSystem::isOnGameThreadOrInit);
      this.pixels = new NativeImage(p_117980_, p_117981_, p_117982_);
      TextureUtil.prepareImage(this.getId(), this.pixels.getWidth(), this.pixels.getHeight());
   }

   public void load(ResourceManager p_117987_) {
   }

   public void upload() {
      if (this.pixels != null) {
         this.bind();
         this.pixels.upload(0, 0, 0, false);
      } else {
         LOGGER.warn("Trying to upload disposed texture {}", (int)this.getId());
      }

   }

   @Nullable
   public NativeImage getPixels() {
      return this.pixels;
   }

   public void setPixels(NativeImage p_117989_) {
      if (this.pixels != null) {
         this.pixels.close();
      }

      this.pixels = p_117989_;
   }

   public void close() {
      if (this.pixels != null) {
         this.pixels.close();
         this.releaseId();
         this.pixels = null;
      }

   }
}