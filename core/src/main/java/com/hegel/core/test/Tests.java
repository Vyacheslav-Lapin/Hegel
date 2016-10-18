package com.hegel.core.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public interface Tests {

    // Символ переноса строки для данной операционной системы
    String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * @param runnable функция
     * @return то, что во время её выполнения попало в поток вывода System.out
     */
    static String fromSystemOutPrint(Runnable runnable) {

        PrintStream realOut = System.out;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(out)) {

            System.setOut(printStream);
            runnable.run();

            return new String(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
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
