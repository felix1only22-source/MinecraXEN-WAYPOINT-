package com.xenbravo.waypointmod;

import com.xenbravo.waypointmod.hud.WaypointHud;
import com.xenbravo.waypointmod.keybind.KeyBindings;
import com.xenbravo.waypointmod.waypoint.WaypointManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaypointMod implements ClientModInitializer {

    public static final String MOD_ID = "xenwaypoints";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static boolean waypointsVisible = true;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[XenWaypoints] Loaded for MC 1.21.10!");
        KeyBindings.register();
        WaypointManager.getInstance().load();
        WaypointHud.register();

        ClientTickEvents.END_CLIENT_TICK.register(this::handleKeys);
    }

    private void handleKeys(MinecraftClient client) {
        if (client.player == null) return;

        // N key → waypoint set screen খুলো
        while (KeyBindings.SET_WAYPOINT.wasPressed()) {
            WaypointManager.getInstance().openNamingScreen(client);
        }

        // B key → toggle on/off
        while (KeyBindings.TOGGLE_WAYPOINTS.wasPressed()) {
            waypointsVisible = !waypointsVisible;
            String status = waypointsVisible ? "§aON" : "§cOFF";
            client.player.sendMessage(
                Text.literal("§6[XenWaypoints] §fDisplay: " + status), true
            );
        }
    }
}
