package me.jlime.holyadmins.client.gui;

import me.jlime.holyadmins.client.gui.components.SidebarManager;
import me.jlime.holyadmins.client.gui.components.ContentManager;
import me.jlime.holyadmins.client.gui.utils.UIConstants;
import me.jlime.holyadmins.client.gui.utils.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Главный экран Holy Admins с современным UI
 * Оптимизированная версия с разделением на компоненты
 */
public class HolyAdminsScreen extends Screen {
    private final SidebarManager sidebarManager;
    private final ContentManager contentManager;
    
    public HolyAdminsScreen() {
        super(Text.literal("Holy Admins"));
        this.sidebarManager = new SidebarManager();
        this.contentManager = new ContentManager();
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Инициализируем кнопки боковой панели
        sidebarManager.initializeDefaultButtons();
        sidebarManager.setOnButtonSelected(contentManager::setCurrentSection);
        
        // Обновляем максимальную прокрутку для текущего размера экрана
        sidebarManager.updateMaxScroll(this.height);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Фон экрана
        context.fill(0, 0, this.width, this.height, UIConstants.BACKGROUND_COLOR);
        
        // Отрисовка боковой панели
        sidebarManager.render(context, this.textRenderer, this.height, mouseX, mouseY);
        
        // Отрисовка основного контента
        contentManager.render(context, this.textRenderer, this.width, this.height, mouseX, mouseY);
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Обработка кликов в боковой панели
        if (sidebarManager.handleMouseClick(mouseX, mouseY, this.height)) {
            return true;
        }
        
        // Обработка кликов в основном контенте
        if (contentManager.handleMouseClick(mouseX, mouseY, button, this.width, this.height)) {
            return true;
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        // Обработка прокрутки в боковой панели
        if (sidebarManager.handleMouseScroll(mouseX, mouseY, amount)) {
            return true;
        }
        
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Закрытие экрана по нажатию Escape
        if (keyCode == 256) { // GLFW_KEY_ESCAPE
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