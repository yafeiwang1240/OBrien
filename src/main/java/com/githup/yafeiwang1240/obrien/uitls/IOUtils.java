package com.githup.yafeiwang1240.obrien.uitls;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
    public static void closeQuietly(Closeable o) {
        try {
            o.close();
        } catch (IOException e) {

        }
    }
}
