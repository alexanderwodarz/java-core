package de.alexanderwodarz.code;

import de.alexanderwodarz.code.log.Level;
import de.alexanderwodarz.code.log.Log;
import de.alexanderwodarz.code.log.ModOutputStream;

import java.io.PrintStream;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class JavaCore {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final PrintStream originalStream = System.out;
    private static PrintStream dummyStream;
    private static PrintStream errorStream;

    public static void main(String[] args) {
        JavaCore.initLog(Level.INFO);
    }

    public static String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static void triggerOutOfMemory() {
        for (int i = 0; i < 100000; i++)
            JavaCore.getRandomBytesAsHex(1000000000);
    }

    public static String getRandomBytesAsHex(int bytes) {
        return toHex(getRandomBytes(bytes));
    }

    public static byte[] getRandomBytes(int bytes) {
        byte[] r = new byte[bytes];
        new Random().nextBytes(r);
        return r;
    }

    public static String toHex(String toHex) {
        return toHex(toHex.getBytes());
    }

    public static String toHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void initLog() {
        initLog(Level.UNKNOWN);
    }

    public static void initLog(Level defaultLevel) {
        if (dummyStream != null && errorStream != null)
            return;
        dummyStream = new ModOutputStream((msg) -> Log.log(msg, defaultLevel));
        System.setOut(dummyStream);
        errorStream = new ModOutputStream((msg) -> Log.log(msg, Level.ERROR));
        System.setErr(errorStream);
    }

    public static int getRandomInt(int length) {
        String result = "";
        for (int i = 0; i < length; i++)
            result += new Random().nextInt(9);
        return Integer.parseInt(result);
    }

    public static int getRandomInt(int start, int stop) {
        int result;
        while (true) {
            int rdm = new Random().nextInt(stop);
            if (rdm >= start) {
                result = rdm;
                break;
            }
        }
        return result;
    }

    public static String getRandomString() {
        return getRandomString(16);
    }

    public static String getRandomString(String allowed) {
        return getRandomString(allowed, 16);
    }

    public static String getRandomString(int length) {
        return getRandomString("abcdegghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", length);
    }

    public static String getRandomString(String allowed, int length) {
        String result = "";
        for (int i = 0; i < length; i++)
            result += allowed.charAt(new Random().nextInt(allowed.length()));
        return result;
    }

    public static PrintStream getOriginalStream() {
        return originalStream;
    }

    public PrintStream getErrorStream() {
        return errorStream;
    }

}
