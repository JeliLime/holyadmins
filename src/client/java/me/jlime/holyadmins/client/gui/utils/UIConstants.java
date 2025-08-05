package me.jlime.holyadmins.client.gui.utils;

/**
 * Константы для UI Holy Admins
 * Содержит все размеры, цвета и другие константы интерфейса
 */
public final class UIConstants {
    
    // Размеры компонентов
    public static final int SIDEBAR_WIDTH = 200;
    public static final int BUTTON_HEIGHT = 35;
    public static final int BUTTON_SPACING = 5;
    public static final int LOGO_HEIGHT = 60;
    public static final int SCROLLBAR_WIDTH = 6;
    public static final int BORDER_RADIUS = 8;
    public static final int BUTTON_RADIUS = 6;
    
    // Отступы
    public static final int PADDING_SMALL = 5;
    public static final int PADDING_MEDIUM = 10;
    public static final int PADDING_LARGE = 20;
    
    // Цвета - Темная тема
    public static final int BACKGROUND_COLOR = 0xFF1a1a1a;
    public static final int SIDEBAR_COLOR = 0xFF2d2d2d;
    public static final int CONTENT_COLOR = 0xFF242424;
    public static final int ACCENT_COLOR = 0xFF6366f1;
    public static final int ACCENT_SECONDARY = 0xFF4f46e5;
    
    // Цвета кнопок
    public static final int BUTTON_COLOR = 0xFF374151;
    public static final int BUTTON_HOVER_COLOR = 0xFF4b5563;
    public static final int BUTTON_SELECTED_COLOR = ACCENT_COLOR;
    
    // Цвета текста
    public static final int TEXT_COLOR = 0xFFe5e7eb;
    public static final int TEXT_SECONDARY = 0xFF9ca3af;
    public static final int TEXT_WHITE = 0xFFFFFFFF;
    
    // Цвета границ и разделителей
    public static final int BORDER_COLOR = 0xFF404040;
    public static final int SCROLLBAR_TRACK_COLOR = 0xFF404040;
    public static final int SCROLLBAR_THUMB_COLOR = ACCENT_COLOR;
    
    // Анимация
    public static final int ANIMATION_DURATION = 200; // мс
    public static final float SCROLL_SPEED = 20.0f;
    
    // Иконки (эмодзи)
    public static final String ICON_CROWN = "👑";
    public static final String ICON_HOME = "🏠";
    public static final String ICON_PLAYERS = "👥";
    public static final String ICON_BANS = "🔨";
    public static final String ICON_KICKS = "👢";
    public static final String ICON_MUTES = "🔇";
    public static final String ICON_WARNS = "⚠️";
    public static final String ICON_REPORTS = "📋";
    public static final String ICON_SETTINGS = "⚙️";
    public static final String ICON_MESSAGES = "💬";
    public static final String ICON_BAN = "🚫";
    public static final String ICON_USERS = "👥";
    public static final String ICON_LOGS = "📄";
    public static final String ICON_ANALYTICS = "📊";
    public static final String ICON_HELP = "❓";
    
    // Приватный конструктор для предотвращения создания экземпляров
    private UIConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}