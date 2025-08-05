# Holy Admins GUI - Структура кода

## Обзор
Главный экран Holy Admins теперь имеет современный UI дизайн с боковой панелью и основной областью контента.

## Структура файла HolyAdminsScreen.java

### Константы UI
```java
// Размеры
SIDEBAR_WIDTH = 200        // Ширина боковой панели
BUTTON_HEIGHT = 35         // Высота кнопок
BUTTON_SPACING = 5         // Отступ между кнопками
LOGO_HEIGHT = 60           // Высота области логотипа

// Цвета (современная темная тема)
BACKGROUND_COLOR = 0xFF1a1a1a     // Основной фон
SIDEBAR_COLOR = 0xFF2d2d2d        // Фон боковой панели
CONTENT_COLOR = 0xFF242424        // Фон контента
ACCENT_COLOR = 0xFF6366f1         // Акцентный цвет (синий)
BUTTON_COLOR = 0xFF374151         // Цвет кнопок
BUTTON_HOVER_COLOR = 0xFF4b5563   // Цвет кнопок при наведении
TEXT_COLOR = 0xFFe5e7eb           // Основной цвет текста
TEXT_SECONDARY = 0xFF9ca3af       // Вторичный цвет текста
```

### Основные компоненты

#### 1. SidebarButton (Внутренний класс)
```java
public final String id;        // Уникальный ID кнопки
public final String text;      // Текст кнопки
public final String icon;      // Иконка (эмодзи)
public final Runnable action;  // Действие при нажатии
```

#### 2. Состояние экрана
```java
List<SidebarButton> sidebarButtons;  // Список кнопок боковой панели
int scrollOffset = 0;                 // Смещение прокрутки
int maxScroll = 0;                    // Максимальное смещение
String selectedButton = "";           // Выбранная кнопка
```

### Методы отрисовки

#### render(DrawContext context, int mouseX, int mouseY, float delta)
Главный метод отрисовки:
1. Отрисовывает фон
2. Вызывает renderSidebar()
3. Вызывает renderMainContent()

#### renderSidebar(DrawContext context, int mouseX, int mouseY)
Отрисовка боковой панели:
1. Фон боковой панели
2. Логотип (renderLogo())
3. Кнопки (renderSidebarButtons())
4. Скроллбар при необходимости (renderScrollbar())
5. Граница

#### renderLogo(DrawContext context)
Отрисовка области логотипа:
1. Градиентный фон
2. Текст "Holy Admins"
3. Иконка короны 👑

#### renderSidebarButtons(DrawContext context, int mouseX, int mouseY)
Отрисовка кнопок:
1. Проверка видимости (учет прокрутки)
2. Определение состояния (наведение, выбор)
3. Отрисовка фона кнопки
4. Отрисовка иконки и текста

#### renderScrollbar(DrawContext context)
Отрисовка скроллбара:
1. Трек скроллбара
2. Ползунок с правильным размером и позицией

#### renderMainContent(DrawContext context, int mouseX, int mouseY)
Отрисовка основного контента:
1. Фон области контента
2. Заголовок раздела
3. Содержимое раздела (renderSelectedContent())

### Обработка событий

#### mouseClicked(double mouseX, double mouseY, int button)
- Обработка кликов по кнопкам боковой панели
- Проверка попадания в область кнопки
- Вызов соответствующего действия

#### mouseScrolled(double mouseX, double mouseY, double amount)
- Прокрутка в области боковой панели
- Ограничение диапазона прокрутки

#### keyPressed(int keyCode, int scanCode, int modifiers)
- Закрытие по клавише Escape

### Вспомогательные методы

#### drawGradientRect()
Отрисовка прямоугольника с градиентом

#### drawRoundedRect()
Отрисовка прямоугольника со скругленными углами

#### interpolateColor()
Интерполяция между двумя цветами

#### getHeaderText()
Получение текста заголовка для выбранного раздела

#### renderSelectedContent()
Отрисовка содержимого выбранного раздела

## Добавление новых кнопок

Для добавления новой кнопки в боковую панель:

1. Добавьте в метод `initSidebarButtons()`:
```java
sidebarButtons.add(new SidebarButton(
    "unique_id",           // Уникальный ID
    "Название кнопки",     // Текст
    "🔧",                  // Иконка (эмодзи)
    () -> selectButton("unique_id")  // Действие
));
```

2. Добавьте обработку в `getHeaderText()`:
```java
case "unique_id": return "🔧 Название кнопки";
```

3. При необходимости добавьте специальную логику в `renderSelectedContent()`

## Настройка цветов

Все цвета определены как константы в начале класса. Для изменения темы просто измените значения констант.

## Прокрутка

Прокрутка автоматически активируется, когда кнопки не помещаются в доступную область. Максимальное смещение рассчитывается автоматически в `initSidebarButtons()`.

## Расширение функциональности

### Добавление подменю
Можно расширить `SidebarButton` для поддержки подменю:
```java
public final List<SidebarButton> submenu;
public final boolean expanded;
```

### Добавление анимаций
Можно добавить анимации переходов, изменив методы отрисовки и добавив временные переменные.

### Кастомные иконки
Вместо эмодзи можно использовать текстуры или SVG иконки, изменив логику отрисовки в `renderSidebarButtons()`.