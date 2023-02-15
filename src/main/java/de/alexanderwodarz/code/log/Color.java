package de.alexanderwodarz.code.log;

public enum Color {
    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),
    BRIGHT_BLACK("\u001B[90m"),
    BRIGHT_RED("\u001B[91m"),
    BRIGHT_GREEN("\u001B[92m"),
    BRIGHT_YELLOW("\u001B[93m"),
    BRIGHT_BLUE("\u001B[94m"),
    BRIGHT_PURPLE("\u001B[95m"),
    BRIGHT_CYAN("\u001B[96m"),
    BRIGHT_WHITE("\u001B[97m"),
    BG_BLACK("\u001B[40m"),
    BG_RED("\u001B[41m"),
    BG_GREEN("\u001B[42m"),
    BG_YELLOW("\u001B[43m"),
    BG_BLUE("\u001B[44m"),
    BG_PURPLE("\u001B[45m"),
    BG_CYAN("\u001B[46m"),
    BG_WHITE("\u001B[47m"),
    BRIGHT_BG_BLACK("\u001B[100m"),
    BRIGHT_BG_RED("\u001B[101m"),
    BRIGHT_BG_GREEN("\u001B[102m"),
    BRIGHT_BG_YELLOW("\u001B[103m"),
    BRIGHT_BG_BLUE("\u001B[104m"),
    BRIGHT_BG_PURPLE("\u001B[105m"),
    BRIGHT_BG_CYAN("\u001B[106m"),
    BRIGHT_BG_WHITE("\u001B[107m");

    private String color;

    private Color(String color) {
        this.color = color;
    }

    public String toString() {
        return color;
    }
}
