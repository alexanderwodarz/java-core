package de.alexanderwodarz.code.log;

public enum Level {
    WARNING("WARNING", Color.YELLOW),
    ERROR("ERROR", Color.RED),
    INFO("INFO", Color.GREEN),
    UNKNOWN("UNKNOWN", Color.CYAN),
    SYSTEM("SYSTEM", Color.BRIGHT_YELLOW);

    private String level;
    private Color color;

    Level(String level, Color color) {
        this.level = level;
        this.color = color;
    }

    public String getLevel() {
        return color + level + Color.RESET;
    }

    public Color getColor() {
        return color;
    }
}
