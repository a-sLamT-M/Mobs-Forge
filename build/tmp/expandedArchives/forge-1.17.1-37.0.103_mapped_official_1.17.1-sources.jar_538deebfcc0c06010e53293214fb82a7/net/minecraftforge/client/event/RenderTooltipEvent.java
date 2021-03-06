/*
 * Minecraft Forge
 * Copyright (c) 2016-2021.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.client.event;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.FormattedText;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * A set of events which are fired at various points during tooltip rendering.
 * <p>
 * Can be used to change the rendering parameters, draw something extra, etc.
 * <p>
 * Do not use this event directly, use one of the subclasses:
 * <ul>
 * <li>{@link RenderTooltipEvent.Pre}</li>
 * <li>{@link RenderTooltipEvent.PostBackground}</li>
 * <li>{@link RenderTooltipEvent.PostText}</li>
 * </ul>
 */
public abstract class RenderTooltipEvent extends net.minecraftforge.eventbus.api.Event
{
    @Nonnull
    protected final ItemStack stack;
    @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
    protected final List<? extends FormattedText> lines;
    protected final PoseStack matrixStack; // TODO: rename in 1.18
    protected int x;
    protected int y;
    protected Font fr;
    protected final List<ClientTooltipComponent> components;

    @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
    public RenderTooltipEvent(@Nonnull ItemStack stack, @Nonnull List<? extends FormattedText> lines, PoseStack matrixStack, int x, int y, @Nonnull Font fr)
    {
        this.stack = stack;
        this.lines = Collections.unmodifiableList(lines);
        this.matrixStack = matrixStack;
        this.x = x;
        this.y = y;
        this.fr = fr;
        this.components = List.of();
    }

    public RenderTooltipEvent(@Nonnull ItemStack stack, PoseStack poseStack, int x, int y, @Nonnull Font font, @Nonnull List<ClientTooltipComponent> components)
    {
        this.stack = stack;
        this.lines = List.of();
        this.components = Collections.unmodifiableList(components);
        this.matrixStack = poseStack;
        this.x = x;
        this.y = y;
        this.fr = font;
    }

    /**
     * @return The stack which the tooltip is being rendered for. As tooltips can be drawn without itemstacks, this stack may be empty.
     */
    @Nonnull
    public ItemStack getStack()
    {
        return stack;
    }
    
    /**
     * The lines to be drawn. May change between {@link RenderTooltipEvent.Pre} and {@link RenderTooltipEvent.Post}.
     * 
     * @return An <i>unmodifiable</i> list of strings. Use {@link net.minecraftforge.event.entity.player.ItemTooltipEvent} to modify tooltip text.
     * @deprecated use {@link #getComponents()}
     */
    @Nonnull
    @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
    public List<? extends FormattedText> getLines()
    {
        return lines;
    }

    /**
     * The components to be drawn.
     * To modify this list use {@link GatherComponents}.
     * @return an unmodifiable list of tooltip components to be drawn.
     */
    @Nonnull
    public List<ClientTooltipComponent> getComponents()
    {
        return components;
    }

    /**
     * @return The MatrixStack of the current rendering context
     */
    public PoseStack getMatrixStack()
    {
        return matrixStack;
    }

    /**
     * @return The X position of the tooltip box. By default, the mouse X position.
     */
    public int getX()
    {
        return x;
    }

    /**
     * @return The Y position of the tooltip box. By default, the mouse Y position.
     */
    public int getY()
    {
        return y;
    }
    
    /**
     * @return The {@link Font} instance the current render is using.
     */
    @Nonnull
    public Font getFontRenderer()
    {
        return fr;
    }

    /**
     * Fires when a tooltip gathers the {@link TooltipComponent}s to render. This event fires before any text wrapping
     * or text processing.
     * This event allows modifying the components to be rendered as well as specifying a maximum width for the tooltip.
     * The maximum width will cause any text components to be wrapped.
     */
    @Cancelable
    public static class GatherComponents extends Event
    {
        private final ItemStack stack;
        private final int screenWidth;
        private final int screenHeight;
        private final List<Either<FormattedText, TooltipComponent>> tooltipElements;
        private int maxWidth;

        public GatherComponents(ItemStack stack, int screenWidth, int screenHeight, List<Either<FormattedText, TooltipComponent>> tooltipElements, int maxWidth)
        {
            this.stack = stack;
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.tooltipElements = tooltipElements;
            this.maxWidth = maxWidth;
        }

        /**
         * @return the ItemStack whose tooltip is being rendered or an empty stack if this tooltip is not for a stack
         */
        public ItemStack getStack()
        {
            return stack;
        }

        /**
         * @return the width of the screen
         */
        public int getScreenWidth()
        {
            return screenWidth;
        }

        /**
         * @return the height of the screen
         */
        public int getScreenHeight()
        {
            return screenHeight;
        }

        /**
         * The elements to be rendered. These can be either formatted text or custom tooltip components.
         * This list is modifiable.
         */
        public List<Either<FormattedText, TooltipComponent>> getTooltipElements()
        {
            return tooltipElements;
        }

        /**
         * @return the current maximum width for the text components of the tooltip (or -1 for no maximum width)
         */
        public int getMaxWidth()
        {
            return maxWidth;
        }

        /**
         * Set the maximum width for the text components of the tooltip (or -1 for no maximum width)
         */
        public void setMaxWidth(int maxWidth)
        {
            this.maxWidth = maxWidth;
        }
    }

    /**
     * This event is fired before any tooltip calculations are done. It provides setters for all aspects of the tooltip, so the final render can be modified.
     * <p>
     * This event is {@link Cancelable}.
     */
    @Cancelable
    public static class Pre extends RenderTooltipEvent
    {
        private int screenWidth;
        private int screenHeight;
        private int maxWidth;

        @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
        public Pre(@Nonnull ItemStack stack, @Nonnull List<? extends FormattedText> lines, PoseStack matrixStack, int x, int y, int screenWidth, int screenHeight, int maxWidth, @Nonnull Font fr)
        {
            super(stack, lines, matrixStack, x, y, fr);
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.maxWidth = maxWidth;
        }

        public Pre(@Nonnull ItemStack stack, PoseStack matrixStack, int x, int y, int screenWidth, int screenHeight, @Nonnull Font font, @Nonnull List<ClientTooltipComponent> components)
        {
            super(stack, matrixStack, x, y, font, components);
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.maxWidth = -1;
        }

        public int getScreenWidth()
        {
            return screenWidth;
        }

        @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
        public void setScreenWidth(int screenWidth)
        {
            this.screenWidth = screenWidth;
        }

        public int getScreenHeight()
        {
            return screenHeight;
        }

        @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
        public void setScreenHeight(int screenHeight)
        {
            this.screenHeight = screenHeight;
        }

        /**
         * @return The max width the tooltip can be. Defaults to -1 (unlimited).
         * @deprecated use {@link GatherComponents}
         */
        @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
        public int getMaxWidth()
        {
            return maxWidth;
        }

        /**
         * Sets the max width of the tooltip. Use -1 for unlimited.
         * @deprecated use {@link GatherComponents}
         */
        @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
        public void setMaxWidth(int maxWidth)
        {
            this.maxWidth = maxWidth;
        }
        
        /**
         * Sets the {@link Font} to be used to render text.
         */
        public void setFontRenderer(@Nonnull Font fr)
        {
            this.fr = fr;
        }

        /**
         * Set the X origin of the tooltip.
         */
        public void setX(int x)
        {
            this.x = x;
        }

        /**
         * Set the Y origin of the tooltip.
         */
        public void setY(int y)
        {
            this.y = y;
        }
    }

    /**
     * Events inheriting from this class are fired at different stages during the tooltip rendering.
     * <p>
     * Do not use this event directly, use one of its subclasses:
     * <ul>
     * <li>{@link RenderTooltipEvent.PostBackground}</li>
     * <li>{@link RenderTooltipEvent.PostText}</li>
     * </ul>
     */
    @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
    protected static abstract class Post extends RenderTooltipEvent
    {
        private final int width;
        private final int height;
        
        public Post(@Nonnull ItemStack stack, @Nonnull List<? extends FormattedText> textLines, PoseStack matrixStack,int x, int y, @Nonnull Font fr, int width, int height)
        {
            super(stack, textLines, matrixStack, x, y, fr);
            this.width = width;
            this.height = height;
        }

        /**
         * @return The width of the tooltip box. This is the width of the <i>inner</i> box, not including the border.
         */
        public int getWidth()
        {
            return width;
        }

        /**
         * @return The height of the tooltip box. This is the height of the <i>inner</i> box, not including the border.
         */
        public int getHeight()
        {
            return height;
        }
    }
    
    /**
     * This event is fired directly after the tooltip background is drawn, but before any text is drawn.
     */
    @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
    public static class PostBackground extends Post 
    {
        public PostBackground(@Nonnull ItemStack stack, @Nonnull List<? extends FormattedText> textLines, PoseStack matrixStack, int x, int y, @Nonnull Font fr, int width, int height)
            { super(stack, textLines, matrixStack, x, y, fr, width, height); }
    }

    /**
     * This event is fired directly after the tooltip text is drawn, but before the GL state is reset.
     */
    @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
    public static class PostText extends Post
    {
        public PostText(@Nonnull ItemStack stack, @Nonnull List<? extends FormattedText> textLines, PoseStack matrixStack, int x, int y, @Nonnull Font fr, int width, int height)
            { super(stack, textLines, matrixStack, x, y, fr, width, height); }
    }
    
    /**
     * This event is fired when the colours for the tooltip background are determined. 
     */
    public static class Color extends RenderTooltipEvent
    {
        private final int originalBackground;
        private final int originalBorderStart;
        private final int originalBorderEnd;
        private int backgroundStart;
        private int backgroundEnd;
        private int borderStart;
        private int borderEnd;

        @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
        public Color(@Nonnull ItemStack stack, @Nonnull List<? extends FormattedText> textLines, PoseStack matrixStack, int x, int y, @Nonnull Font fr, int background, int borderStart, int borderEnd)
        {
            super(stack, textLines, matrixStack, x, y, fr);
            this.originalBackground = background;
            this.originalBorderStart = borderStart;
            this.originalBorderEnd = borderEnd;
            this.backgroundStart = background;
            this.backgroundEnd = background;
            this.borderStart = borderStart;
            this.borderEnd = borderEnd;
        }

        public Color(@Nonnull ItemStack stack, PoseStack matrixStack, int x, int y, @Nonnull Font fr, int background, int borderStart, int borderEnd, @Nonnull List<ClientTooltipComponent> components)
        {
            super(stack, matrixStack, x, y, fr, components);
            this.originalBackground = background;
            this.originalBorderStart = borderStart;
            this.originalBorderEnd = borderEnd;
            this.backgroundStart = background;
            this.backgroundEnd = background;
            this.borderStart = borderStart;
            this.borderEnd = borderEnd;
        }

        /**
         * @deprecated use {@link #getBackgroundStart()} and {@link #getBackgroundEnd}
         */
        @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
        public int getBackground()
        {
            return backgroundStart;
        }

        public int getBackgroundStart()
        {
            return backgroundStart;
        }

        public int getBackgroundEnd()
        {
            return backgroundEnd;
        }

        public void setBackground(int background)
        {
            this.backgroundStart = background;
            this.backgroundEnd = background;
        }

        public void setBackgroundStart(int backgroundStart)
        {
            this.backgroundStart = backgroundStart;
        }

        public void setBackgroundEnd(int backgroundEnd)
        {
            this.backgroundEnd = backgroundEnd;
        }

        public int getBorderStart()
        {
            return borderStart;
        }

        public void setBorderStart(int borderStart)
        {
            this.borderStart = borderStart;
        }

        public int getBorderEnd()
        {
            return borderEnd;
        }

        public void setBorderEnd(int borderEnd)
        {
            this.borderEnd = borderEnd;
        }

        @Deprecated(since = "1.18", forRemoval = true) // TODO: remove in 1.18
        public int getOriginalBackground()
        {
            return originalBackground;
        }

        public int getOriginalBackgroundStart()
        {
            return originalBackground;
        }

        public int getOriginalBackgroundEnd()
        {
            return originalBackground;
        }

        public int getOriginalBorderStart()
        {
            return originalBorderStart;
        }

        public int getOriginalBorderEnd()
        {
            return originalBorderEnd;
        }
    }
}
