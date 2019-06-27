package com.githup.yafeiwang1240.obrien.uitls;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
    public static void closeQuietly(Closeable o) {
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
