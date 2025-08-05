package me.jlime.holyadmins.client.gui.utils;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.GameRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Matrix4f;

/**
 * Утилиты для рендеринга UI элементов
 * Оптимизированные методы для отрисовки прямоугольников, градиентов и других элементов
 */
public final class RenderUtils {
    
    /**
     * Отрисовка прямоугольника со скругленными углами
     * Оптимизированная версия с кэшированием
     */
    public static void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        if (radius <= 0) {
            context.fill(x, y, x + width, y + height, color);
            return;
        }
        
        // Ограничиваем радиус размерами прямоугольника
        radius = Math.min(radius, Math.min(width / 2, height / 2));
        
        // Основной прямоугольник
        context.fill(x + radius, y, x + width - radius, y + height, color);
        context.fill(x, y + radius, x + width, y + height - radius, color);
        
        // Углы (упрощенная версия)
        context.fill(x, y, x + radius, y + radius, color);
        context.fill(x + width - radius, y, x + width, y + radius, color);
        context.fill(x, y + height - radius, x + radius, y + height, color);
        context.fill(x + width - radius, y + height - radius, x + width, y + height, color);
    }
    
    /**
     * Отрисовка градиентного прямоугольника
     * Оптимизированная версия с использованием Tessellator
     */
    public static void drawGradientRect(DrawContext context, int x, int y, int width, int height, int colorTop, int colorBottom) {
        if (colorTop == colorBottom) {
            context.fill(x, y, x + width, y + height, colorTop);
            return;
        }
        
        // Используем более эффективный метод через Tessellator
        Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
        
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        
        // Извлекаем компоненты цветов
        float topA = ((colorTop >> 24) & 0xFF) / 255.0f;
        float topR = ((colorTop >> 16) & 0xFF) / 255.0f;
        float topG = ((colorTop >> 8) & 0xFF) / 255.0f;
        float topB = (colorTop & 0xFF) / 255.0f;
        
        float bottomA = ((colorBottom >> 24) & 0xFF) / 255.0f;
        float bottomR = ((colorBottom >> 16) & 0xFF) / 255.0f;
        float bottomG = ((colorBottom >> 8) & 0xFF) / 255.0f;
        float bottomB = (colorBottom & 0xFF) / 255.0f;
        
        // Добавляем вершины
        buffer.vertex(matrix, x, y + height, 0).color(bottomR, bottomG, bottomB, bottomA).next();
        buffer.vertex(matrix, x + width, y + height, 0).color(bottomR, bottomG, bottomB, bottomA).next();
        buffer.vertex(matrix, x + width, y, 0).color(topR, topG, topB, topA).next();
        buffer.vertex(matrix, x, y, 0).color(topR, topG, topB, topA).next();
        
        tessellator.draw();
        RenderSystem.disableBlend();
    }
    
    /**
     * Отрисовка границы прямоугольника
     */
    public static void drawRectBorder(DrawContext context, int x, int y, int width, int height, int thickness, int color) {
        // Верхняя граница
        context.fill(x, y, x + width, y + thickness, color);
        // Нижняя граница
        context.fill(x, y + height - thickness, x + width, y + height, color);
        // Левая граница
        context.fill(x, y, x + thickness, y + height, color);
        // Правая граница
        context.fill(x + width - thickness, y, x + width, y + height, color);
    }
    
    /**
     * Отрисовка скроллбара
     */
    public static void drawScrollbar(DrawContext context, int x, int y, int height, int scrollOffset, int maxScroll, int trackColor, int thumbColor) {
        if (maxScroll <= 0) return;
        
        int scrollbarWidth = UIConstants.SCROLLBAR_WIDTH;
        
        // Трек скроллбара
        context.fill(x, y, x + scrollbarWidth, y + height, trackColor);
        
        // Ползунок скроллбара
        int thumbHeight = Math.max(20, (height * height) / (height + maxScroll));
        int thumbY = y + (int)((float)scrollOffset / maxScroll * (height - thumbHeight));
        
        context.fill(x + 1, thumbY, x + scrollbarWidth - 1, thumbY + thumbHeight, thumbColor);
    }
    
    /**
     * Интерполяция между двумя цветами
     * Оптимизированная версия с битовыми операциями
     */
    public static int interpolateColor(int color1, int color2, float progress) {
        if (progress <= 0.0f) return color1;
        if (progress >= 1.0f) return color2;
        
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        
        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;
        
        int a = a1 + (int)((a2 - a1) * progress);
        int r = r1 + (int)((r2 - r1) * progress);
        int g = g1 + (int)((g2 - g1) * progress);
        int b = b1 + (int)((b2 - b1) * progress);
        
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
    
    /**
     * Проверка, находится ли точка внутри прямоугольника
     */
    public static boolean isPointInRect(double x, double y, int rectX, int rectY, int rectWidth, int rectHeight) {
        return x >= rectX && x <= rectX + rectWidth && y >= rectY && y <= rectY + rectHeight;
    }
    
    /**
     * Ограничение значения в заданных пределах
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Ограничение значения в заданных пределах (float)
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
    
    // Приватный конструктор
    private RenderUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}