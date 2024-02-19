package de.alexanderwodarz.code.log;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

public class ModOutputStream extends PrintStream {
    private ModStreamListener listener;

    public ModOutputStream(ModStreamListener listener, OutputStream stream) {
        super(stream);
        this.listener = listener;
    }

    public ModOutputStream(ModStreamListener listener) {
        super(new ByteArrayOutputStream());
        this.listener = listener;
    }


    public void print(boolean b) {
        this.print(b ? "true" : "false");
    }

    public void print(char c) {
        this.print(String.valueOf(c));
    }

    public void print(int i) {
        this.print(String.valueOf(i));
    }

    public void print(long l) {
        this.print(String.valueOf(l));
    }

    public void print(float f) {
        this.print(String.valueOf(f));
    }

    public void print(double d) {
        this.print(String.valueOf(d));
    }

    public void print(char s[]) {
        this.print(s);
    }

    public void print(String s) {
        for (String msg : s.split("\n")) {
            for (String msg2 : msg.split("\r")) {
                listener.onMessage(msg2);
            }
        }
    }

    public void print(Object obj) {
        this.print(String.valueOf(obj));
    }

    public void println() {
        this.print("\n");
    }

    public void println(boolean x) {
        this.println(x + "");
    }

    public void println(char x) {
        this.println(x + "");
    }

    public void println(int x) {
        this.println(x + "");
    }

    public void println(long x) {
        this.println(x + "");
    }

    public void println(float x) {
        this.println(x);
    }

    public void println(double x) {
        this.println(x + "");
    }

    public void println(char x[]) {
        this.println(x + "");
    }

    public void println(String x) {
        synchronized (this) {
            this.print(x + "\n");
        }
    }

    public void println(Object x) {
        String s = x + "";
        synchronized (this) {
            this.print(s + "\n");
        }
    }

    public PrintStream printf(String format, Object... args) {
        return format(format, args);
    }

    public PrintStream printf(Locale l, String format, Object... args) {
        return format(l, format, args);
    }

    public PrintStream append(CharSequence csq) {
        if (csq == null)
            this.print("null");
        else
            this.print(csq.toString());
        return this;
    }

    public PrintStream append(CharSequence csq, int start, int end) {
        CharSequence cs = (csq == null ? "null" : csq);
        this.print("" + cs.subSequence(start, end).toString());
        return this;
    }

    public PrintStream append(char c) {
        this.print(c);
        return this;
    }
}
