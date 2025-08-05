package me.jlime.holyadmins.client.gui.components;

import me.jlime.holyadmins.client.gui.utils.UIConstants;
import me.jlime.holyadmins.client.gui.utils.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер основного контента
 * Управляет отображением различных разделов в правой части экрана
 */
public class ContentManager {
    private String currentSection;
    private final Map<String, ContentRenderer> contentRenderers;
    
    public ContentManager() {
        this.currentSection = "";
        this.contentRenderers = new HashMap<>();
        initializeDefaultRenderers();
    }
    
    /**
     * Устанавливает текущий раздел
     */
    public void setCurrentSection(String section) {
        this.currentSection = section != null ? section : "";
    }
    
    /**
     * Получает текущий раздел
     */
    public String getCurrentSection() {
        return currentSection;
    }
    
    /**
     * Регистрирует рендерер для определенного раздела
     */
    public void registerContentRenderer(String sectionId, ContentRenderer renderer) {
        contentRenderers.put(sectionId, renderer);
    }
    
    /**
     * Отрисовывает основной контент
     */
    public void render(DrawContext context, TextRenderer textRenderer, int screenWidth, int screenHeight, int mouseX, int mouseY) {
        int contentX = UIConstants.SIDEBAR_WIDTH + UIConstants.PADDING_MEDIUM;
        int contentY = UIConstants.PADDING_MEDIUM;
        int contentWidth = screenWidth - UIConstants.SIDEBAR_WIDTH - UIConstants.PADDING_MEDIUM * 2;
        int contentHeight = screenHeight - UIConstants.PADDING_MEDIUM * 2;
        
        // Фон основного контента
        RenderUtils.drawRoundedRect(context, contentX, contentY, contentWidth, contentHeight, 
                UIConstants.BORDER_RADIUS, UIConstants.CONTENT_COLOR);
        
        // Заголовок
        String headerText = getHeaderText();
        Text header = Text.literal(headerText);
        context.drawText(textRenderer, header, 
                contentX + UIConstants.PADDING_LARGE, contentY + UIConstants.PADDING_LARGE, 
                UIConstants.TEXT_COLOR, true);
        
        // Контент
        int contentAreaX = contentX + UIConstants.PADDING_LARGE;
        int contentAreaY = contentY + UIConstants.PADDING_LARGE * 2 + 10; // +10 для высоты заголовка
        int contentAreaWidth = contentWidth - UIConstants.PADDING_LARGE * 2;
        int contentAreaHeight = contentHeight - UIConstants.PADDING_LARGE * 3 - 10;
        
        renderContent(context, textRenderer, contentAreaX, contentAreaY, contentAreaWidth, contentAreaHeight, mouseX, mouseY);
    }
    
    /**
     * Получает текст заголовка для текущего раздела
     */
    private String getHeaderText() {
        switch (currentSection) {
            case "messages": return UIConstants.ICON_MESSAGES + " Переписка";
            case "settings": return UIConstants.ICON_SETTINGS + " Настройки";
            case "ban_reasons": return UIConstants.ICON_BAN + " Причины бана";
            case "users": return UIConstants.ICON_USERS + " Пользователи";
            case "reports": return UIConstants.ICON_REPORTS + " Жалобы";
            case "logs": return UIConstants.ICON_LOGS + " Логи";
            case "analytics": return UIConstants.ICON_ANALYTICS + " Аналитика";
            case "help": return UIConstants.ICON_HELP + " Помощь";
            default: return UIConstants.ICON_CROWN + " Holy Admins Panel";
        }
    }
    
    /**
     * Отрисовывает контент текущего раздела
     */
    private void renderContent(DrawContext context, TextRenderer textRenderer, int x, int y, int width, int height, int mouseX, int mouseY) {
        ContentRenderer renderer = contentRenderers.get(currentSection);
        if (renderer != null) {
            renderer.render(context, textRenderer, x, y, width, height, mouseX, mouseY);
        } else {
            renderDefaultContent(context, textRenderer, x, y, width, height);
        }
    }
    
    /**
     * Отрисовывает контент по умолчанию
     */
    private void renderDefaultContent(DrawContext context, TextRenderer textRenderer, int x, int y, int width, int height) {
        if (currentSection.isEmpty()) {
            renderWelcomeScreen(context, textRenderer, x, y);
        } else {
            renderPlaceholderContent(context, textRenderer, x, y);
        }
    }
    
    /**
     * Отрисовывает экран приветствия
     */
    private void renderWelcomeScreen(DrawContext context, TextRenderer textRenderer, int x, int y) {
        String[] welcomeLines = {
            "Добро пожаловать в Holy Admins!",
            "",
            "Выберите раздел из меню слева для начала работы.",
            "",
            "Доступные разделы:",
            "• Переписка - управление сообщениями",
            "• Настройки - конфигурация системы",
            "• Причины бана - управление банами",
            "• Пользователи - управление пользователями",
            "• Жалобы - обработка жалоб",
            "• Логи - просмотр логов системы",
            "• Аналитика - статистика и отчеты",
            "• Помощь - справочная информация"
        };
        
        int lineY = y;
        for (String line : welcomeLines) {
            int color = line.startsWith("•") ? UIConstants.ACCENT_COLOR : UIConstants.TEXT_COLOR;
            context.drawText(textRenderer, Text.literal(line), x, lineY, color, false);
            lineY += 15;
        }
    }
    
    /**
     * Отрисовывает заглушку для раздела
     */
    private void renderPlaceholderContent(DrawContext context, TextRenderer textRenderer, int x, int y) {
        String headerText = getHeaderText();
        Text placeholder = Text.literal("Содержимое раздела \"" + headerText + "\" будет здесь.");
        context.drawText(textRenderer, placeholder, x, y, UIConstants.TEXT_SECONDARY, false);
        
        Text instruction = Text.literal("Этот раздел готов для дальнейшей разработки.");
        context.drawText(textRenderer, instruction, x, y + 20, UIConstants.TEXT_SECONDARY, false);
        
        // Дополнительная информация
        Text info = Text.literal("ID раздела: " + currentSection);
        context.drawText(textRenderer, info, x, y + 50, UIConstants.TEXT_SECONDARY, false);
    }
    
    /**
     * Инициализирует рендереры по умолчанию
     */
    private void initializeDefaultRenderers() {
        // Можно добавить специальные рендереры для разных разделов
        // Например:
        // registerContentRenderer("settings", new SettingsRenderer());
        // registerContentRenderer("users", new UsersRenderer());
    }
    
    /**
     * Обрабатывает клик мыши в области контента
     */
    public boolean handleMouseClick(double mouseX, double mouseY, int button, int screenWidth, int screenHeight) {
        int contentX = UIConstants.SIDEBAR_WIDTH + UIConstants.PADDING_MEDIUM;
        int contentY = UIConstants.PADDING_MEDIUM;
        int contentWidth = screenWidth - UIConstants.SIDEBAR_WIDTH - UIConstants.PADDING_MEDIUM * 2;
        int contentHeight = screenHeight - UIConstants.PADDING_MEDIUM * 2;
        
        // Проверяем, что клик в области контента
        if (!RenderUtils.isPointInRect(mouseX, mouseY, contentX, contentY, contentWidth, contentHeight)) {
            return false;
        }
        
        ContentRenderer renderer = contentRenderers.get(currentSection);
        if (renderer != null) {
            int contentAreaX = contentX + UIConstants.PADDING_LARGE;
            int contentAreaY = contentY + UIConstants.PADDING_LARGE * 2 + 10;
            int contentAreaWidth = contentWidth - UIConstants.PADDING_LARGE * 2;
            int contentAreaHeight = contentHeight - UIConstants.PADDING_LARGE * 3 - 10;
            
            return renderer.handleMouseClick(mouseX - contentAreaX, mouseY - contentAreaY, 
                    button, contentAreaWidth, contentAreaHeight);
        }
        
        return false;
    }
    
    /**
     * Интерфейс для рендереров контента
     */
    public interface ContentRenderer {
        void render(DrawContext context, TextRenderer textRenderer, int x, int y, int width, int height, int mouseX, int mouseY);
        
        default boolean handleMouseClick(double mouseX, double mouseY, int button, int width, int height) {
            return false;
        }
        
        default boolean handleKeyPress(int keyCode, int scanCode, int modifiers) {
            return false;
        }
    }
}