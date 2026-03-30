package com.xenbravo.waypointmod.waypoint;

public class Waypoint {
    private final String name;
    private final double x, y, z;

    public Waypoint(String name, double x, double y, double z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distanceTo(double px, double py, double pz) {
        double dx = x - px, dy = y - py, dz = z - pz;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    public String getName() { return name; }
    public double getX()    { return x; }
    public double getY()    { return y; }
    public double getZ()    { return z; }

    public String toJsonLine() {
        return String.format("{\"n\":\"%s\",\"x\":%.1f,\"y\":%.1f,\"z\":%.1f}",
            name.replace("\"", "'"), x, y, z);
    }

    public static Waypoint parse(String line) {
        try {
            String n = extract(line, "n");
            double x = Double.parseDouble(extract(line, "x"));
            double y = Double.parseDouble(extract(line, "y"));
            double z = Double.parseDouble(extract(line, "z"));
            return new Waypoint(n, x, y, z);
        } catch (Exception e) {
            return null;
        }
    }

    private static String extract(String json, String key) {
        String search = "\"" + key + "\":";
        int i = json.indexOf(search) + search.length();
        boolean quoted = json.charAt(i) == '"';
        if (quoted) {
            int end = json.indexOf('"', i + 1);
            return json.substring(i + 1, end);
        } else {
            int end = json.indexOf(',', i);
            if (end == -1) end = json.indexOf('}', i);
            return json.substring(i, end).trim();
        }
    }
}
