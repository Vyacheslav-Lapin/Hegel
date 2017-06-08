package com.hegel.core.test;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public interface Tests {

    String LINE_SEPARATOR = System.getProperty("line.separator");

    @SneakyThrows
    static String fromSystemOutPrint(Runnable runnable) {

        PrintStream realOut = System.out;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(out)) {

            System.setOut(printStream);
            runnable.run();

            return new String(out.toByteArray());

        } finally {
            System.setOut(realOut);
        }
    }

    static String fromSystemOutPrintln(Runnable runnable) {
        String s = fromSystemOutPrint(runnable);
        if (s.endsWith(LINE_SEPARATOR))
            s = s.substring(0, s.length() - LINE_SEPARATOR.length());
        return s;
    }
}
