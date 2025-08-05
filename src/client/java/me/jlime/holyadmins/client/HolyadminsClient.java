package me.jlime.holyadmins.client;

import net.fabricmc.api.ClientModInitializer;

public class HolyadminsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register key bindings
        KeyBindingHandler.register();
    }
}
