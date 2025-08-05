package me.jlime.holyadmins.client.gui.components;

import me.jlime.holyadmins.client.gui.utils.UIConstants;
import me.jlime.holyadmins.client.gui.utils.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Менеджер боковой панели
 * Управляет кнопками, прокруткой и отрисовкой боковой панели
 */
public class SidebarManager {
    private final List<SidebarButton> buttons;
    private String selectedButtonId;
    private int scrollOffset;
    private int maxScroll;
    private Consumer<String> onButtonSelected;
    
    public SidebarManager() {
        this.buttons = new ArrayList<>();
        this.scrollOffset = 0;
        this.maxScroll = 0;
        this.selectedButtonId = "";
    }
    
    public void initializeDefaultButtons() {
        // Очищаем существующие кнопки
        buttons.clear();
        
        // Добавляем кнопки по умолчанию
        addButton(new SidebarButton("home", "Главная", UIConstants.ICON_HOME, () -> selectButton("home")));
        addButton(new SidebarButton("players", "Игроки", UIConstants.ICON_PLAYERS, () -> selectButton("players")));
        addButton(new SidebarButton("bans", "Баны", UIConstants.ICON_BANS, () -> selectButton("bans")));
        addButton(new SidebarButton("kicks", "Кики", UIConstants.ICON_KICKS, () -> selectButton("kicks")));
        addButton(new SidebarButton("mutes", "Муты", UIConstants.ICON_MUTES, () -> selectButton("mutes")));
        addButton(new SidebarButton("warns", "Предупреждения", UIConstants.ICON_WARNS, () -> selectButton("warns")));
        addButton(new SidebarButton("reports", "Жалобы", UIConstants.ICON_REPORTS, () -> selectButton("reports")));
        addButton(new SidebarButton("settings", "Настройки", UIConstants.ICON_SETTINGS, () -> selectButton("settings")));
        
        // Выбираем первую кнопку по умолчанию
        if (!buttons.isEmpty()) {
            selectButton(buttons.get(0).getId());
        }
    }
    
    /**
     * Добавляет кнопку в боковую панель
     */
    public void addButton(SidebarButton button) {
        if (button != null && !buttons.contains(button)) {
            buttons.add(button);
            calculateMaxScroll();
        }
    }
    
    /**
     * Удаляет кнопку из боковой панели
     */
    public void removeButton(String buttonId) {
        buttons.removeIf(button -> button.id.equals(buttonId));
        calculateMaxScroll();
    }
    
    /**
     * Получает кнопку по ID
     */
    public SidebarButton getButton(String buttonId) {
        return buttons.stream()
                .filter(button -> button.id.equals(buttonId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Устанавливает выбранную кнопку
     */
    public void selectButton(String buttonId) {
        this.selectedButtonId = buttonId;
        if (onButtonSelected != null) {
            onButtonSelected.accept(buttonId);
        }
    }
    
    /**
     * Получает ID выбранной кнопки
     */
    public String getSelectedButtonId() {
        return selectedButtonId;
    }
    
    /**
     * Устанавливает обработчик выбора кнопки
     */
    public void setOnButtonSelected(Consumer<String> onButtonSelected) {
        this.onButtonSelected = onButtonSelected;
    }
    

    
    /**
     * Отрисовывает боковую панель
     */
    public void render(DrawContext context, TextRenderer textRenderer, int screenHeight, int mouseX, int mouseY) {
        // Фон боковой панели
        context.fill(0, 0, UIConstants.SIDEBAR_WIDTH, screenHeight, UIConstants.SIDEBAR_COLOR);
        
        // Логотип
        renderLogo(context, textRenderer);
        
        // Кнопки
        renderButtons(context, textRenderer, screenHeight, mouseX, mouseY);
        
        // Скроллбар
        if (maxScroll > 0) {
            renderScrollbar(context, screenHeight);
        }
        
        // Граница
        context.fill(UIConstants.SIDEBAR_WIDTH - 1, 0, UIConstants.SIDEBAR_WIDTH, screenHeight, UIConstants.BORDER_COLOR);
    }
    
    /**
     * Отрисовывает логотип
     */
    private void renderLogo(DrawContext context, TextRenderer textRenderer) {
        // Фон логотипа с градиентом
        RenderUtils.drawGradientRect(context, 
                UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, 
                UIConstants.SIDEBAR_WIDTH - UIConstants.PADDING_MEDIUM * 2, UIConstants.LOGO_HEIGHT, 
                UIConstants.ACCENT_COLOR, UIConstants.ACCENT_SECONDARY);
        
        // Текст логотипа
        Text logoText = Text.literal("Holy Admins");
        int logoWidth = textRenderer.getWidth(logoText);
        int logoX = (UIConstants.SIDEBAR_WIDTH - logoWidth) / 2;
        int logoY = UIConstants.PADDING_MEDIUM + (UIConstants.LOGO_HEIGHT - 9) / 2;
        
        context.drawText(textRenderer, logoText, logoX, logoY, UIConstants.TEXT_WHITE, true);
        
        // Иконка логотипа
        Text logoIcon = Text.literal(UIConstants.ICON_CROWN);
        int iconWidth = textRenderer.getWidth(logoIcon);
        int iconX = (UIConstants.SIDEBAR_WIDTH - iconWidth) / 2;
        context.drawText(textRenderer, logoIcon, iconX, logoY - 15, UIConstants.TEXT_WHITE, false);
    }
    
    /**
     * Отрисовывает кнопки
     */
    private void renderButtons(DrawContext context, TextRenderer textRenderer, int screenHeight, int mouseX, int mouseY) {
        int buttonY = UIConstants.LOGO_HEIGHT + UIConstants.PADDING_LARGE + UIConstants.PADDING_MEDIUM - scrollOffset;
        
        for (SidebarButton button : buttons) {
            if (!button.isVisible()) continue;
            
            // Пропускаем невидимые кнопки
            if (buttonY + UIConstants.BUTTON_HEIGHT < UIConstants.LOGO_HEIGHT + UIConstants.PADDING_LARGE || 
                buttonY > screenHeight) {
                buttonY += UIConstants.BUTTON_HEIGHT + UIConstants.BUTTON_SPACING;
                continue;
            }
            
            boolean isHovered = RenderUtils.isPointInRect(mouseX, mouseY, 
                    UIConstants.PADDING_MEDIUM, buttonY, 
                    UIConstants.SIDEBAR_WIDTH - UIConstants.PADDING_MEDIUM * 2, UIConstants.BUTTON_HEIGHT);
            boolean isSelected = button.id.equals(selectedButtonId);
            
            // Цвет кнопки
            int buttonColor;
            if (!button.isEnabled()) {
                buttonColor = UIConstants.BUTTON_COLOR & 0x7FFFFFFF; // Полупрозрачный
            } else if (isSelected) {
                buttonColor = UIConstants.BUTTON_SELECTED_COLOR;
            } else if (isHovered) {
                buttonColor = UIConstants.BUTTON_HOVER_COLOR;
            } else {
                buttonColor = UIConstants.BUTTON_COLOR;
            }
            
            // Фон кнопки
            RenderUtils.drawRoundedRect(context, 
                    UIConstants.PADDING_MEDIUM, buttonY, 
                    UIConstants.SIDEBAR_WIDTH - UIConstants.PADDING_MEDIUM * 2, UIConstants.BUTTON_HEIGHT, 
                    UIConstants.BUTTON_RADIUS, buttonColor);
            
            // Текст кнопки
            int textColor = button.isEnabled() ? UIConstants.TEXT_COLOR : UIConstants.TEXT_SECONDARY;
            
            // Иконка
            context.drawText(textRenderer, Text.literal(button.icon), 
                    UIConstants.PADDING_LARGE, buttonY + UIConstants.PADDING_MEDIUM, textColor, false);
            
            // Текст
            context.drawText(textRenderer, Text.literal(button.text), 
                    UIConstants.PADDING_LARGE * 2, buttonY + UIConstants.PADDING_MEDIUM, textColor, false);
            
            buttonY += UIConstants.BUTTON_HEIGHT + UIConstants.BUTTON_SPACING;
        }
    }
    
    /**
     * Отрисовывает скроллбар
     */
    private void renderScrollbar(DrawContext context, int screenHeight) {
        int scrollbarX = UIConstants.SIDEBAR_WIDTH - UIConstants.SCROLLBAR_WIDTH - 2;
        int scrollbarY = UIConstants.LOGO_HEIGHT + UIConstants.PADDING_LARGE;
        int scrollbarHeight = screenHeight - UIConstants.LOGO_HEIGHT - UIConstants.PADDING_LARGE * 2;
        
        RenderUtils.drawScrollbar(context, scrollbarX, scrollbarY, scrollbarHeight, 
                scrollOffset, maxScroll, UIConstants.SCROLLBAR_TRACK_COLOR, UIConstants.SCROLLBAR_THUMB_COLOR);
    }
    
    /**
     * Обрабатывает клик мыши
     */
    public boolean handleMouseClick(double mouseX, double mouseY, int screenHeight) {
        if (mouseX < 0 || mouseX > UIConstants.SIDEBAR_WIDTH) return false;
        
        int buttonY = UIConstants.LOGO_HEIGHT + UIConstants.PADDING_LARGE + UIConstants.PADDING_MEDIUM - scrollOffset;
        
        for (SidebarButton button : buttons) {
            if (!button.isVisible() || !button.isEnabled()) {
                buttonY += UIConstants.BUTTON_HEIGHT + UIConstants.BUTTON_SPACING;
                continue;
            }
            
            if (buttonY + UIConstants.BUTTON_HEIGHT < UIConstants.LOGO_HEIGHT + UIConstants.PADDING_LARGE || 
                buttonY > screenHeight) {
                buttonY += UIConstants.BUTTON_HEIGHT + UIConstants.BUTTON_SPACING;
                continue;
            }
            
            if (RenderUtils.isPointInRect(mouseX, mouseY, 
                    UIConstants.PADDING_MEDIUM, buttonY, 
                    UIConstants.SIDEBAR_WIDTH - UIConstants.PADDING_MEDIUM * 2, UIConstants.BUTTON_HEIGHT)) {
                button.performAction();
                return true;
            }
            
            buttonY += UIConstants.BUTTON_HEIGHT + UIConstants.BUTTON_SPACING;
        }
        
        return false;
    }
    
    /**
     * Обрабатывает прокрутку мыши
     */
    public boolean handleMouseScroll(double mouseX, double mouseY, double amount) {
        if (mouseX < UIConstants.SIDEBAR_WIDTH && maxScroll > 0) {
            scrollOffset = RenderUtils.clamp(
                    scrollOffset - (int)(amount * UIConstants.SCROLL_SPEED), 
                    0, maxScroll);
            return true;
        }
        return false;
    }
    
    /**
     * Вычисляет максимальное смещение прокрутки
     */
    private void calculateMaxScroll() {
        int visibleButtons = (int) buttons.stream().filter(SidebarButton::isVisible).count();
        int totalButtonsHeight = visibleButtons * UIConstants.BUTTON_HEIGHT + 
                                (visibleButtons - 1) * UIConstants.BUTTON_SPACING;
        int availableHeight = 600 - UIConstants.LOGO_HEIGHT - UIConstants.PADDING_LARGE * 2; // Примерная высота экрана
        
        maxScroll = Math.max(0, totalButtonsHeight - availableHeight);
    }
    
    /**
     * Обновляет максимальную прокрутку для заданной высоты экрана
     */
    public void updateMaxScroll(int screenHeight) {
        int visibleButtons = (int) buttons.stream().filter(SidebarButton::isVisible).count();
        int totalButtonsHeight = visibleButtons * UIConstants.BUTTON_HEIGHT + 
                                (visibleButtons - 1) * UIConstants.BUTTON_SPACING;
        int availableHeight = screenHeight - UIConstants.LOGO_HEIGHT - UIConstants.PADDING_LARGE * 2;
        
        maxScroll = Math.max(0, totalButtonsHeight - availableHeight);
        scrollOffset = RenderUtils.clamp(scrollOffset, 0, maxScroll);
    }
}