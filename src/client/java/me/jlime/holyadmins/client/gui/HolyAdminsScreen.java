package me.jlime.holyadmins.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import net.minecraft.client.MinecraftClient;

public class HolyAdminsScreen extends Screen {
    private static final Identifier SHADER_LOCATION = new Identifier("holyadmins", "shaders/core/rounded_rect");
    
    private final int windowWidth = 400;
    private final int windowHeight = 300;
    private float animationProgress = 0.0f;
    private long openTime;
    
    public HolyAdminsScreen() {
        super(Text.literal("Holy Admins"));
        this.openTime = System.currentTimeMillis();
    }
    
    @Override
    protected void init() {
        super.init();
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Update animation
        long currentTime = System.currentTimeMillis();
        float timeSinceOpen = (currentTime - openTime) / 1000.0f;
        animationProgress = Math.min(1.0f, timeSinceOpen * 3.0f); // 3 second animation
        
        // Apply easing function for smooth animation
        float easedProgress = easeOutCubic(animationProgress);
        
        // Calculate window position (centered)
        int windowX = (this.width - windowWidth) / 2;
        int windowY = (this.height - windowHeight) / 2;
        
        // Apply scale animation
        float scale = 0.8f + (0.2f * easedProgress);
        
        context.getMatrices().push();
        context.getMatrices().translate(windowX + windowWidth / 2.0f, windowY + windowHeight / 2.0f, 0);
        context.getMatrices().scale(scale, scale, 1.0f);
        context.getMatrices().translate(-windowWidth / 2.0f, -windowHeight / 2.0f, 0);
        
        // Render the main window with rounded corners and shadow
        renderRoundedWindow(context, 0, 0, windowWidth, windowHeight, easedProgress);
        
        // Render title
        Text title = Text.literal("Holy Admins Panel");
        int titleWidth = this.textRenderer.getWidth(title);
        context.drawText(this.textRenderer, title, 
            (windowWidth - titleWidth) / 2, 20, 
            0xFFFFFF, true);
        
        // Render content area
        renderContent(context, easedProgress);
        
        context.getMatrices().pop();
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    private void renderRoundedWindow(DrawContext context, int x, int y, int width, int height, float alpha) {
        // Try to use custom shader for better effects
        if (tryRenderWithShader(context, x, y, width, height, alpha)) {
            return;
        }
        
        // Fallback to manual rendering
        // Draw multiple shadow layers for depth
        for (int i = 0; i < 3; i++) {
            int shadowOffset = 2 + i * 2;
            int shadowAlpha = (int)((0x20000000 * alpha) / (i + 1)) & 0xFF000000;
            drawRoundedRect(context, x + shadowOffset, y + shadowOffset, width, height, 12 + i, shadowAlpha);
        }
        
        // Draw main window background with gradient effect
        drawGradientRoundedRect(context, x, y, width, height, 10, 0xFF1a1a1a, 0xFF2a2a2a);
        
        // Draw animated border with glow effect
        float time = System.currentTimeMillis() / 1000.0f;
        int glowIntensity = (int)(128 + 64 * Math.sin(time * 2));
        int borderColor = (glowIntensity << 16) | (glowIntensity/2 << 8) | (glowIntensity/3);
        drawRoundedRectBorder(context, x, y, width, height, 10, 0xFF000000 | borderColor, 2);
        
        // Draw animated top accent line with gradient
        int accentColor1 = 0xFF6B46C1; // Purple
        int accentColor2 = 0xFF8B5CF6; // Lighter purple
        drawGradientRoundedRect(context, x, y, width, 4, 10, accentColor1, accentColor2);
        
        // Add subtle inner glow
        drawRoundedRectBorder(context, x + 1, y + 1, width - 2, height - 2, 9, 0x20FFFFFF, 1);
    }
    
    private boolean tryRenderWithShader(DrawContext context, int x, int y, int width, int height, float alpha) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            
            // Set up shader uniforms
            float time = System.currentTimeMillis() / 1000.0f;
            
            // Use the custom shader for rendering
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            
            // Create vertices for the rounded rectangle
            Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            
            // Define quad vertices with colors
            int color = 0xFF1a1a1a;
            float r = ((color >> 16) & 0xFF) / 255.0f;
            float g = ((color >> 8) & 0xFF) / 255.0f;
            float b = (color & 0xFF) / 255.0f;
            float a = alpha;
            
            bufferBuilder.vertex(matrix, x, y + height, 0).color(r, g, b, a).next();
            bufferBuilder.vertex(matrix, x + width, y + height, 0).color(r, g, b, a).next();
            bufferBuilder.vertex(matrix, x + width, y, 0).color(r, g, b, a).next();
            bufferBuilder.vertex(matrix, x, y, 0).color(r, g, b, a).next();
            
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            
            RenderSystem.disableBlend();
            return true;
        } catch (Exception e) {
            // If shader fails, return false to use fallback
            return false;
        }
    }
    
    private void renderContent(DrawContext context, float alpha) {
        // Content area background with gradient
        int contentY = 50;
        int contentHeight = windowHeight - 70;
        drawGradientRoundedRect(context, 10, contentY, windowWidth - 20, contentHeight, 8, 0xFF2a2a2a, 0xFF1a1a1a);
        
        // Add inner border for content area
        drawRoundedRectBorder(context, 10, contentY, windowWidth - 20, contentHeight, 8, 0xFF404040, 1);
        
        // Sample content with animations
        Text[] lines = {
            Text.literal("✨ Welcome to Holy Admins! ✨"),
            Text.literal(""),
            Text.literal("🎨 This is a beautiful GUI with:"),
            Text.literal("  • Rounded corners with shadows"),
            Text.literal("  • Smooth scale animations"),
            Text.literal("  • Modern dark theme"),
            Text.literal("  • Gradient effects"),
            Text.literal("  • Animated glow borders"),
            Text.literal(""),
            Text.literal("⌨️ Press Left Ctrl or ESC to close")
        };
        
        float time = System.currentTimeMillis() / 1000.0f;
        int lineY = contentY + 20;
        
        for (int i = 0; i < lines.length; i++) {
            Text line = lines[i];
            
            // Animate text appearance
            float lineAlpha = Math.min(1.0f, alpha * 2.0f - (i * 0.1f));
            if (lineAlpha <= 0) continue;
            
            // Color based on content
            int textColor = 0xCCCCCC;
            if (line.getString().contains("Welcome")) {
                // Animated rainbow effect for welcome text
                float hue = (time + i * 0.5f) % 1.0f;
                textColor = hsvToRgb(hue, 0.7f, 1.0f) | 0xFF000000;
            } else if (line.getString().contains("•") || line.getString().contains("⌨️") || line.getString().contains("🎨")) {
                textColor = 0xFF8B5CF6; // Purple accent
            }
            
            // Apply alpha
            int alpha8bit = (int)(lineAlpha * 255);
            textColor = (textColor & 0x00FFFFFF) | (alpha8bit << 24);
            
            // Slight animation offset
            float xOffset = (float)Math.sin(time + i * 0.3f) * 2.0f;
            
            context.drawText(this.textRenderer, line, 
                (int)(25 + xOffset), lineY, textColor, false);
            lineY += 15;
        }
        
        // Add a subtle animated particle effect
        renderParticleEffect(context, 10, contentY, windowWidth - 20, contentHeight, time, alpha);
    }
    
    private void renderParticleEffect(DrawContext context, int x, int y, int width, int height, float time, float alpha) {
        // Simple floating particles
        for (int i = 0; i < 8; i++) {
            float particleTime = time + i * 0.8f;
            float particleX = x + 20 + (float)Math.sin(particleTime * 0.5f) * (width - 40);
            float particleY = y + 20 + (float)Math.cos(particleTime * 0.3f + i) * (height - 40);
            
            int particleAlpha = (int)(alpha * 64 * (0.5f + 0.5f * Math.sin(particleTime * 2)));
            int particleColor = 0x6B46C1 | (particleAlpha << 24);
            
            // Draw small glowing dot
            context.fill((int)particleX, (int)particleY, (int)particleX + 2, (int)particleY + 2, particleColor);
        }
    }
    
    private int hsvToRgb(float h, float s, float v) {
        int i = (int)(h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);
        
        float r, g, b;
        switch (i % 6) {
            case 0: r = v; g = t; b = p; break;
            case 1: r = q; g = v; b = p; break;
            case 2: r = p; g = v; b = t; break;
            case 3: r = p; g = q; b = v; break;
            case 4: r = t; g = p; b = v; break;
            case 5: r = v; g = p; b = q; break;
            default: r = g = b = 0;
        }
        
        return ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
    }
    
    private void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        // Simple rounded rectangle implementation
        // Main rectangle
        context.fill(x + radius, y, x + width - radius, y + height, color);
        context.fill(x, y + radius, x + width, y + height - radius, color);
        
        // Corners (simplified)
        context.fill(x, y, x + radius, y + radius, color);
        context.fill(x + width - radius, y, x + width, y + radius, color);
        context.fill(x, y + height - radius, x + radius, y + height, color);
        context.fill(x + width - radius, y + height - radius, x + width, y + height, color);
    }
    
    private void drawGradientRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int colorTop, int colorBottom) {
        // Draw gradient effect by drawing multiple horizontal lines
        for (int i = 0; i < height; i++) {
            float progress = (float) i / height;
            int currentColor = interpolateColor(colorTop, colorBottom, progress);
            
            int lineY = y + i;
            int lineWidth = width;
            int lineX = x;
            
            // Adjust for rounded corners
            if (i < radius) {
                int cornerOffset = radius - (int)Math.sqrt(radius * radius - (radius - i) * (radius - i));
                lineX += cornerOffset;
                lineWidth -= 2 * cornerOffset;
            } else if (i >= height - radius) {
                int cornerOffset = radius - (int)Math.sqrt(radius * radius - (i - (height - radius)) * (i - (height - radius)));
                lineX += cornerOffset;
                lineWidth -= 2 * cornerOffset;
            }
            
            if (lineWidth > 0) {
                context.fill(lineX, lineY, lineX + lineWidth, lineY + 1, currentColor);
            }
        }
    }
    
    private int interpolateColor(int color1, int color2, float progress) {
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        
        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;
        
        int a = (int)(a1 + (a2 - a1) * progress);
        int r = (int)(r1 + (r2 - r1) * progress);
        int g = (int)(g1 + (g2 - g1) * progress);
        int b = (int)(b1 + (b2 - b1) * progress);
        
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
    
    private void drawRoundedRectBorder(DrawContext context, int x, int y, int width, int height, int radius, int color, int thickness) {
        // Top border
        context.fill(x + radius, y, x + width - radius, y + thickness, color);
        // Bottom border
        context.fill(x + radius, y + height - thickness, x + width - radius, y + height, color);
        // Left border
        context.fill(x, y + radius, x + thickness, y + height - radius, color);
        // Right border
        context.fill(x + width - thickness, y + radius, x + width, y + height - radius, color);
        
        // Corner borders (simplified)
        context.fill(x, y, x + radius + thickness, y + radius + thickness, color);
        context.fill(x + width - radius - thickness, y, x + width, y + radius + thickness, color);
        context.fill(x, y + height - radius - thickness, x + radius + thickness, y + height, color);
        context.fill(x + width - radius - thickness, y + height - radius - thickness, x + width, y + height, color);
    }
    
    private float easeOutCubic(float t) {
        return 1 - (float)Math.pow(1 - t, 3);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Close on Escape or Left Ctrl again
        if (keyCode == 256 || keyCode == 341) { // Escape or Left Ctrl
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public boolean shouldPause() {
        return false; // Don't pause the game
    }
    
    @Override
    public void close() {
        super.close();
    }
}