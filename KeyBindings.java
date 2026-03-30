package com.xenbravo.waypointmod.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static KeyBinding SET_WAYPOINT;
    public static KeyBinding TOGGLE_WAYPOINTS;

    public static void register() {
        SET_WAYPOINT = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.xenwaypoints.set",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "key.categories.xenwaypoints"
        ));

        TOGGLE_WAYPOINTS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.xenwaypoints.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.xenwaypoints"
        ));
    }
}
