package me.jlime.holyadmins.client.gui.components;

import java.util.Objects;

/**
 * Класс для представления кнопки боковой панели
 * Содержит всю информацию о кнопке и её поведении
 */
public class SidebarButton {
    public final String id;
    public final String text;
    public final String icon;
    public final Runnable action;
    private boolean enabled;
    private boolean visible;
    
    /**
     * Создает новую кнопку боковой панели
     * @param id Уникальный идентификатор кнопки
     * @param text Текст кнопки
     * @param icon Иконка кнопки (эмодзи или текст)
     * @param action Действие, выполняемое при нажатии
     */
    public SidebarButton(String id, String text, String icon, Runnable action) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.text = Objects.requireNonNull(text, "Text cannot be null");
        this.icon = Objects.requireNonNull(icon, "Icon cannot be null");
        this.action = Objects.requireNonNull(action, "Action cannot be null");
        this.enabled = true;
        this.visible = true;
    }
    
    /**
     * Проверяет, активна ли кнопка
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Устанавливает состояние активности кнопки
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Получает ID кнопки
     */
    public String getId() {
        return id;
    }
    
    /**
     * Проверяет, видима ли кнопка
     */
    public boolean isVisible() {
        return visible;
    }
    
    /**
     * Устанавливает видимость кнопки
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    /**
     * Выполняет действие кнопки, если она активна
     */
    public void performAction() {
        if (enabled && action != null) {
            action.run();
        }
    }
    
    /**
     * Получает полный текст кнопки с иконкой
     */
    public String getFullText() {
        return icon + " " + text;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SidebarButton that = (SidebarButton) obj;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "SidebarButton{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", icon='" + icon + '\'' +
                ", enabled=" + enabled +
                ", visible=" + visible +
                '}';
    }
}