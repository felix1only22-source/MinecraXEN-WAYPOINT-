package com.xenbravo.waypointmod.waypoint;

import com.xenbravo.waypointmod.WaypointMod;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class WaypointManager {

    private static final WaypointManager INSTANCE = new WaypointManager();
    private final List<Waypoint> waypoints = new ArrayList<>();
    private Path savePath;

    public static WaypointManager getInstance() { return INSTANCE; }

    private WaypointManager() {}

    private Path getSavePath(MinecraftClient client) {
        if (savePath == null) {
            savePath = client.runDirectory.toPath()
                .resolve("xenwaypoints").resolve("waypoints.txt");
        }
        return savePath;
    }

    public void addWaypoint(Waypoint wp, MinecraftClient client) {
        waypoints.add(wp);
        save(client);
        WaypointMod.LOGGER.info("[XenWaypoints] Saved: " + wp.getName());
    }

    public List<Waypoint> getWaypoints() { return waypoints; }

    public void openNamingScreen(MinecraftClient client) {
        if (client.player == null) return;
        double x = client.player.getX();
        double y = client.player.getY();
        double z = client.player.getZ();
        client.execute(() ->
            client.setScreen(new WaypointNamingScreen(x, y, z))
        );
    }

    public void save(MinecraftClient client) {
        try {
            Path path = getSavePath(client);
            Files.createDirectories(path.getParent());
            List<String> lines = new ArrayList<>();
            for (Waypoint wp : waypoints) lines.add(wp.toJsonLine());
            Files.write(path, lines);
        } catch (IOException e) {
            WaypointMod.LOGGER.error("[XenWaypoints] Save failed: " + e.getMessage());
        }
    }

    public void load() {
        // Load হবে game directory ready হওয়ার পর (first screen open এ)
        // এই moment এ client.runDirectory available না, তাই lazy load করি
    }

    public void loadFromClient(MinecraftClient client) {
        try {
            Path path = getSavePath(client);
            if (!Files.exists(path)) return;
            waypoints.clear();
            for (String line : Files.readAllLines(path)) {
                if (line.isBlank()) continue;
                Waypoint wp = Waypoint.parse(line);
                if (wp != null) waypoints.add(wp);
            }
            WaypointMod.LOGGER.info("[XenWaypoints] Loaded " + waypoints.size() + " waypoints.");
        } catch (IOException e) {
            WaypointMod.LOGGER.error("[XenWaypoints] Load failed: " + e.getMessage());
        }
    }
}
