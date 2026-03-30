package com.xenbravo.waypointmod.hud;

import com.xenbravo.waypointmod.WaypointMod;
import com.xenbravo.waypointmod.waypoint.Waypoint;
import com.xenbravo.waypointmod.waypoint.WaypointManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class WaypointHud {

    public static void register() {
        HudRenderCallback.EVENT.register((ctx, tickDelta) -> render(ctx));
    }

    private static void render(DrawContext ctx) {
        if (!WaypointMod.waypointsVisible) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;
        if (mc.options.debugEnabled) return; // F3 screen এ hide

        List<Waypoint> wps = WaypointManager.getInstance().getWaypoints();
        if (wps.isEmpty()) return;

        TextRenderer tr = mc.textRenderer;
        int W = mc.getWindow().getScaledWidth();
        int H = mc.getWindow().getScaledHeight();

        double px = mc.player.getX();
        double py = mc.player.getY();
        double pz = mc.player.getZ();

        // ── Layout constants ──────────────────────────────
        int ENTRY_H   = 36;   // প্রতিটা waypoint এর height
        int PAD_X     = 5;    // inner horizontal padding
        int PAD_Y     = 4;    // inner vertical padding
        int ACCENT_W  = 3;    // left color bar width
        int MARGIN    = 6;    // screen edge margin

        int count   = wps.size();
        int totalH  = count * ENTRY_H + (count - 1) * 2; // 2px gap between entries
        int startY  = (H - totalH) / 2; // vertically centered

        for (int i = 0; i < count; i++) {
            Waypoint wp = wps.get(i);
            double dist = wp.distanceTo(px, py, pz);

            String nameLine  = "§e" + wp.getName();
            String coordLine = "§7" + (int)wp.getX() + ", " + (int)wp.getY() + ", " + (int)wp.getZ();
            String distLine  = "§f" + formatDist(dist);

            // Max text width for this entry
            int textW = Math.max(tr.getWidth(nameLine),
                        Math.max(tr.getWidth(coordLine), tr.getWidth(distLine)));

            int boxW = ACCENT_W + PAD_X + textW + PAD_X;
            int boxH = ENTRY_H;

            int boxRight = W - MARGIN;
            int boxLeft  = boxRight - boxW;
            int boxTop   = startY + i * (ENTRY_H + 2);

            // Background
            ctx.fill(boxLeft, boxTop, boxRight, boxTop + boxH, 0xAA000000);

            // Left accent bar (orange)
            ctx.fill(boxLeft, boxTop, boxLeft + ACCENT_W, boxTop + boxH, 0xFFFF9900);

            // Text X (after accent bar + padding)
            int textX = boxLeft + ACCENT_W + PAD_X;
            int lineY1 = boxTop + PAD_Y;
            int lineY2 = lineY1 + 10;
            int lineY3 = lineY2 + 10;

            ctx.drawTextWithShadow(tr, nameLine,  textX, lineY1, 0xFFFFFF);
            ctx.drawTextWithShadow(tr, coordLine, textX, lineY2, 0xFFFFFF);
            ctx.drawTextWithShadow(tr, distLine,  textX, lineY3, 0xFFFFFF);
        }

        // Bottom-right hint
        String hint = "§8[B] Waypoints  [N] Add";
        ctx.drawTextWithShadow(
            tr, hint,
            W - tr.getWidth(hint) - MARGIN,
            H - 12,
            0xFFFFFF
        );
    }

    private static String formatDist(double d) {
        if (d >= 1000) return String.format("%.1fkm", d / 1000.0);
        return String.format("%.0fm", d);
    }
}
