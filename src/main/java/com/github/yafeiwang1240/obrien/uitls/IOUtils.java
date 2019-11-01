package com.github.yafeiwang1240.obrien.uitls;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
    public static void closeQuietly(Closeable o) {
        if (o == null) return;
        try {
            o.close();
        } catch (Exception e) {
            // ignore
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            // ignore
        }
    }
}
