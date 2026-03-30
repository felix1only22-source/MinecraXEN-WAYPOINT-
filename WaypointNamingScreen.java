package com.xenbravo.waypointmod.waypoint;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class WaypointNamingScreen extends Screen {

    private final double x, y, z;
    private TextFieldWidget nameField;
    private boolean loaded = false;

    public WaypointNamingScreen(double x, double y, double z) {
        super(Text.literal("Set Waypoint"));
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;

        // Text field — নাম লেখার জায়গা
        nameField = new TextFieldWidget(
            this.textRenderer,
            cx - 100, cy - 10,
            200, 20,
            Text.literal("Name")
        );
        nameField.setMaxLength(32);
        nameField.setFocused(true);
        nameField.setPlaceholder(Text.literal("§7Enter waypoint name..."));
        this.addDrawableChild(nameField);

        // Save button
        this.addDrawableChild(
            ButtonWidget.builder(Text.literal("§aSave"), b -> saveAndClose())
                .dimensions(cx - 52, cy + 16, 50, 18).build()
        );

        // Cancel button
        this.addDrawableChild(
            ButtonWidget.builder(Text.literal("§cCancel"), b -> this.close())
                .dimensions(cx + 2, cy + 16, 50, 18).build()
        );

        // Waypoints load করো (first time)
        if (!loaded) {
            WaypointManager.getInstance().loadFromClient(MinecraftClient.getInstance());
            loaded = true;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) { // Enter / Numpad Enter
            saveAndClose();
            return true;
        }
        if (keyCode == 256) { // Escape
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void saveAndClose() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) name = "Waypoint";

        MinecraftClient client = MinecraftClient.getInstance();
        Waypoint wp = new Waypoint(name, x, y, z);
        WaypointManager.getInstance().addWaypoint(wp, client);

        if (client.player != null) {
            client.player.sendMessage(
                net.minecraft.text.Text.literal(
                    "§6[XenWaypoints] §aSaved: §e" + name +
                    " §7(" + (int)x + ", " + (int)y + ", " + (int)z + ")"
                ), true
            );
        }
        this.close();
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // Dim background
        this.renderBackground(ctx, mouseX, mouseY, delta);

        int cx = this.width / 2;
        int cy = this.height / 2;

        // Box
        ctx.fill(cx - 118, cy - 38, cx + 118, cy + 40, 0xD0000000);
        // Top accent bar (orange)
        ctx.fill(cx - 118, cy - 38, cx + 118, cy - 36, 0xFFFF9900);

        // Title
        ctx.drawCenteredTextWithShadow(
            this.textRenderer, Text.literal("§6§lSet Waypoint"),
            cx, cy - 32, 0xFFFFFF
        );

        // Coordinates
        ctx.drawCenteredTextWithShadow(
            this.textRenderer,
            Text.literal("§8" + (int)x + "  " + (int)y + "  " + (int)z),
            cx, cy - 22, 0xAAAAAA
        );

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
