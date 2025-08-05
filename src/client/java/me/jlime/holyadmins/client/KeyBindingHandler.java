package me.jlime.holyadmins.client;

import me.jlime.holyadmins.client.gui.HolyAdminsScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindingHandler {
    private static KeyBinding openGuiKey;
    private static boolean wasPressed = false;
    
    public static void register() {
        // Register the keybinding for Left Ctrl
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.holyadmins.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,
            "category.holyadmins.general"
        ));
        
        // Register tick event to handle key presses
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            
            boolean isPressed = openGuiKey.isPressed();
            
            // Check if key was just pressed (not held)
            if (isPressed && !wasPressed) {
                // Open the GUI
                MinecraftClient.getInstance().setScreen(new HolyAdminsScreen());
            }
            
            wasPressed = isPressed;
        });
    }
}